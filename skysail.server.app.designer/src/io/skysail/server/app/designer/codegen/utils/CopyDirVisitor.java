//package io.skysail.server.app.designer.codegen.utils;
//
//import java.io.IOException;
//import java.nio.file.FileVisitResult;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.SimpleFileVisitor;
//import java.nio.file.StandardCopyOption;
//import java.nio.file.attribute.BasicFileAttributes;
//
//public class CopyDirVisitor extends SimpleFileVisitor<Path> {
//    
//    private Path fromPath;
//    private Path toPath;
//    private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;
//
//    public CopyDirVisitor(Path from, Path to) {
//        this.fromPath = from;
//        this.toPath = to;
//    }
//
//    @Override
//    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//        Path targetPath = toPath.resolve(fromPath.relativize(dir));
//        if (!Files.exists(targetPath)) {
//            Files.createDirectory(targetPath);
//        }
//        return FileVisitResult.CONTINUE;
//    }
//
//    @Override
//    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//        Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
//        return FileVisitResult.CONTINUE;
//    }
//}