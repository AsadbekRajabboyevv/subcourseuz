package uz.asadbek.subcourse.filestorage;

import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtils {

    public static final String MIME_X_MS_DOWNLOAD = "application/x-msdownload";
    public static final String MIME_X_EXECUTABLE = "application/x-executable";
    public static final String MIME_X_SH = "application/x-sh";
    public static final String MIME_X_PHP = "application/x-php";
    public static final String JSP = "jsp";
    public static final String PHP = "php";
    public static final String PYTHON = "py";
    public static final String CMD = "cmd";
    public static final String BAT = "bat";
    public static final String EXE = "exe";
    public static final String SH = "sh";
    public static final String PS1 = "ps1";

    public static final Set<String> APPLICATION_BLOCKED_MIMES = Set.of(
        MIME_X_MS_DOWNLOAD,
        MIME_X_EXECUTABLE,
        MIME_X_SH,
        MIME_X_PHP
    );

    public static final Set<String> APPLICATION_BLOCKED_EXTENSIONS = Set.of(
        JSP, EXE, BAT, SH, CMD, PS1, PHP, PYTHON
    );

}
