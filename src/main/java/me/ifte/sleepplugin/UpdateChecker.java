public class UpdateChecke{

public static boolean hasSpigotUpdate(String resourceId) {
        boolean hasUpdate = false;
        try (java.io.InputStream inputStream =
                     new java.net.URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
             java.util.Scanner scanner = new java.util.Scanner(inputStream)) {
            if (scanner.hasNext())
                hasUpdate = !Sleepplugin.getPlugin().getDescription().getVersion().equalsIgnoreCase(scanner.next());
        } catch (java.io.IOException ioException) {
            ioException.printStackTrace();
            hasUpdate = false;
        }
        return hasUpdate;
    }
}
