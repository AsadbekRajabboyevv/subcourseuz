package uz.asadbek.subcourse.filestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadOptions {
    private String folder;          // "avatars/", "courses/"
    private boolean publicAccess;   // CDN / public URL

    public FileUploadOptions setTestImages() {
        this.folder = "test-images/";
        this.publicAccess = true;
        return this;
    }

    public FileUploadOptions setTestOptionsImages() {
        this.folder = "test-answers/";
        this.publicAccess = true;
        return this;
    }

    public FileUploadOptions setTestQuestions() {
        this.folder = "test-questions/";
        this.publicAccess = true;
        return this;
    }

    public FileUploadOptions setCourseImages() {
        this.folder = "course-images/";
        this.publicAccess = true;
        return this;
    }

    public FileUploadOptions setScienceImages() {
        this.folder = "science-images/";
        this.publicAccess = true;
        return this;
    }

    public FileUploadOptions setLessonFiles() {
        this.folder = "lesson-files/";
        this.publicAccess = true;
        return this;
    }

    public FileUploadOptions setAvatarImages() {
        this.folder = "avatars/";
        this.publicAccess = true;
        return this;
    }

    public FileUploadOptions setTopUpRequestImages() {
        this.folder = "top-up-request-images/";
        this.publicAccess = false;
        return this;
    }
}
