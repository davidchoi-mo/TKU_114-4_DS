abstract class MediaFile {
    private final String fileName;
    private final double sizeMb;

    MediaFile(String fileName, double sizeMb) {
        this.fileName = fileName == null || fileName.trim().isEmpty()
                ? "unknown.file" : fileName.trim();
        this.sizeMb = Math.max(0, sizeMb);
    }

    String getFileName() {
        return fileName;
    }

    double getSizeMb() {
        return sizeMb;
    }

    abstract String getMediaType();

    String describe() {
        return String.format("%s｜%s｜%.1f MB",
                getMediaType(), fileName, sizeMb);
    }
}

interface Playable {
    String play();
}

interface Compressible {
    String compress();
}

class ImageFile extends MediaFile implements Compressible {
    ImageFile(String fileName, double sizeMb) {
        super(fileName, sizeMb);
    }

    @Override
    String getMediaType() {
        return "圖片";
    }

    @Override
    public String compress() {
        return String.format("壓縮圖片 %s：%.1f MB → %.1f MB",
                getFileName(), getSizeMb(), getSizeMb() * 0.65);
    }
}

class AudioFile extends MediaFile implements Playable, Compressible {
    AudioFile(String fileName, double sizeMb) {
        super(fileName, sizeMb);
    }

    @Override
    String getMediaType() {
        return "音訊";
    }

    @Override
    public String play() {
        return "播放音訊：" + getFileName();
    }

    @Override
    public String compress() {
        return String.format("壓縮音訊 %s：%.1f MB → %.1f MB",
                getFileName(), getSizeMb(), getSizeMb() * 0.70);
    }
}

class VideoFile extends MediaFile implements Playable, Compressible {
    VideoFile(String fileName, double sizeMb) {
        super(fileName, sizeMb);
    }

    @Override
    String getMediaType() {
        return "影片";
    }

    @Override
    public String play() {
        return "播放影片：" + getFileName();
    }

    @Override
    public String compress() {
        return String.format("壓縮影片 %s：%.1f MB → %.1f MB",
                getFileName(), getSizeMb(), getSizeMb() * 0.50);
    }
}

public class MediaProcessingSystem {
    public static void main(String[] args) {
        MediaFile[] mediaFiles = {
            new ImageFile("campus.jpg", 8.0),
            new AudioFile("lecture.mp3", 25.0),
            new VideoFile("demo.mp4", 240.0),
            new ImageFile("logo.png", -5.0)
        };

        for (MediaFile media : mediaFiles) {
            System.out.println(media.describe());

            if (media instanceof Playable playable) {
                System.out.println("  " + playable.play());
            } else {
                System.out.println("  不支援播放");
            }

            if (media instanceof Compressible compressible) {
                System.out.println("  " + compressible.compress());
            }
        }
    }
}
