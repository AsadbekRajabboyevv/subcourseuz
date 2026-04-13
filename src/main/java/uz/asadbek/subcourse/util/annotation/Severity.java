package uz.asadbek.subcourse.util.annotation;

import jakarta.validation.Payload;

public class Severity {
    public static class INFO implements Payload {}
    public static class WARNING implements Payload {}
    public static class ERROR implements Payload {}
}
