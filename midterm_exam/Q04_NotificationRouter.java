public class Q04_NotificationRouter {

    public interface Channel {
        String name();

        boolean supports(String destination);

        String send(String destination, String message);
    }

    public static class EmailChannel implements Channel {

        @Override
        public String name() {
            return "EMAIL";
        }

        @Override
        public boolean supports(String destination) {
            if (destination == null) {
                return false;
            }

            int atIndex = destination.indexOf('@');

            return atIndex > 0 && atIndex < destination.length() - 1;
        }

        @Override
        public String send(String destination, String message) {
            return name() + "|" + destination + "|" + message;
        }
    }

    public static class SmsChannel implements Channel {

        @Override
        public String name() {
            return "SMS";
        }

        @Override
        public boolean supports(String destination) {
            if (destination == null) {
                return false;
            }

            String phoneNumber = destination.replace("-", "");

            return phoneNumber.matches("\\d{10}");
        }

        @Override
        public String send(String destination, String message) {
            return name() + "|" + destination + "|" + message;
        }
    }

    public static java.util.List<String> route(
            java.util.List<Channel> channels,
            String destination,
            String message
    ) {
        java.util.List<String> results = new java.util.ArrayList<>();

        if (channels == null || destination == null || message == null) {
            return results;
        }

        for (Channel channel : channels) {
            if (channel != null && channel.supports(destination)) {
                results.add(channel.send(destination, message));
            }
        }

        return results;
    }
}