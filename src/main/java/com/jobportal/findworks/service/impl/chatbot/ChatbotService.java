package com.jobportal.findworks.service.impl.chatbot;


import com.jobportal.findworks.entity.User;
import com.jobportal.findworks.entity.chatbot.ChatMessage;
import com.jobportal.findworks.entity.chatbot.ChatSession;
import com.jobportal.findworks.repository.UserRepository;
import com.jobportal.findworks.repository.chatbot.ChatMessageRepository;
import com.jobportal.findworks.repository.chatbot.ChatSessionRepository;
import com.jobportal.findworks.service.CatalogService;
import com.jobportal.findworks.service.LocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    private final LocationService locationService;
    private final CatalogService catalogService;

    @Transactional
    public String handleMessage(Long userId, String userMessageRaw) {
        String userMessage = (userMessageRaw == null) ? "" : userMessageRaw.trim();
        if (userMessage.isBlank()) return "Please type something.";

        User user = userRepository.getReferenceById(userId);

        ChatSession session = chatSessionRepository.findTop1ByUser_IdOrderByCreatedAtDesc(userId)
                .orElseGet(() -> {
                    ChatSession s = new ChatSession();
                    s.setUser(user);
                    return chatSessionRepository.save(s);
                });

        // save USER message
        ChatMessage m1 = new ChatMessage();
        m1.setSession(session);
        m1.setSender(ChatMessage.Sender.USER);
        m1.setMessage(userMessage);
        chatMessageRepository.save(m1);

        String reply = buildReply(user, userMessage);

        // save BOT message
        ChatMessage m2 = new ChatMessage();
        m2.setSession(session);
        m2.setSender(ChatMessage.Sender.BOT);
        m2.setMessage(reply);
        chatMessageRepository.save(m2);

        return reply;
    }

    /*private String buildReply(User user, String message) {
        String m = message.toLowerCase(Locale.ROOT);

        if (m.contains("hi") || m.contains("hello") || m.contains("hey")) {
            return "Hi! You can ask:\n- find jobs\n- post a job\n- my applications";
        }

        if (m.contains("my applications")) {
            return "Open: /worker/applications";
        }

        if (m.contains("post job") || m.contains("post a job") || m.contains("create job")) {
            if (user.getRole() == User.Role.EMPLOYER) {
                return "You can post a job here: /employer/jobs/new";
            }
            return "Only employers can post jobs. If you are a worker, you can search and apply on /jobs.";
        }

        if (m.contains("find job") || m.contains("find jobs") || m.contains("jobs")) {
            // simple reply for MVP
            return "You can browse jobs here: /jobs\nTip: use City & Category filters on the page.";
        }

        return "Sorry, I didn't understand. Try: 'find jobs', 'post a job', or 'my applications'.";
    }*/

    private String buildReply(User user, String message) {
        String m = message == null ? "" : message.trim();
        String ml = m.toLowerCase(Locale.ROOT);

        if (ml.isBlank()) return "Please type something.";

        if (ml.matches(".*\\b(hi|hello|hey)\\b.*")) {
            return "Hi! Try:\n- electrician jobs in chennai\n- jobs in hyderabad\n- post a job\n- my applications";
        }

        if (ml.contains("my applications")) {
            return "Open: /worker/applications";
        }

        if (ml.contains("post a job") || ml.contains("post job") || ml.contains("create job")) {
            if (user.getRole() == User.Role.EMPLOYER) return "Open: /employer/jobs/new";
            return "Only employers can post jobs. You can browse jobs here: /jobs";
        }

        // FIND JOBS intent (broad)
        if (ml.contains("job")) {
            Match city = matchCity(ml);
            Match category = matchCategory(ml);

            String url = buildJobsUrl(city, category);

            if (city != null && category != null) {
                return "Showing " + category.name + " jobs in " + city.name + ".\nOpen: " + url;
            }
            if (city != null) {
                return "Showing jobs in " + city.name + ".\nOpen: " + url;
            }
            if (category != null) {
                return "Showing " + category.name + " jobs.\nOpen: " + url;
            }

            // no match -> default jobs (your worker default-city logic will still work)
            return "Open: /jobs\nTip: You can type 'electrician jobs in chennai'.";
        }

        return "Sorry, I didn't understand.\nTry: 'electrician jobs in chennai' or 'post a job'.";
    }

    private record Match(Long id, String name) {}

    private String buildJobsUrl(Match city, Match category) {
        String url = "/jobs";
        String sep = "?";

        if (city != null) {
            url += sep + "cityId=" + city.id;
            sep = "&";
        }
        if (category != null) {
            url += sep + "categoryId=" + category.id;
        }
        return url;
    }

    private Match matchCategory(String ml) {
        // Simple synonyms (optional but helpful)
        if (ml.contains("electric")) ml = ml + " electrician";
        if (ml.contains("plumb")) ml = ml + " plumber";
        if (ml.contains("paint")) ml = ml + " painter";

        var categories = catalogService.listActiveCategories();
        String normMsg = normalize(ml);

        for (var c : categories) {
            String n1 = normalize(c.getName()); // "Electrician"
            String n2 = normalize(c.getCode()); // "ELECTRICIAN"
            if (normMsg.contains(n1) || normMsg.contains(n2)) {
                return new Match(c.getId(), c.getName());
            }
        }
        return null;
    }

    private Match matchCity(String ml) {
        // City aliases for common names vs DB names
        // (extend anytime)
        if (ml.contains("bangalore")) ml = ml.replace("bangalore", "bengaluru");
        if (ml.contains("mysore")) ml = ml.replace("mysore", "mysuru");
        if (ml.contains("mangalore")) ml = ml.replace("mangalore", "mangaluru");
        if (ml.contains("cochin")) ml = ml.replace("cochin", "kochi");
        if (ml.contains("trivandrum")) ml = ml.replace("trivandrum", "thiruvananthapuram");

        var cities = locationService.listAllCities();
        String normMsg = normalize(ml);

        for (var c : cities) {
            String n = normalize(c.getName());
            if (normMsg.contains(n)) {
                return new Match(c.getId(), c.getName());
            }
        }
        return null;
    }

    private String normalize(String s) {
        return (s == null) ? "" : s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }
}
