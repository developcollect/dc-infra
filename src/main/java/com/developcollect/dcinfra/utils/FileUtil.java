package com.developcollect.dcinfra.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2020/10/14 11:34
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {


    public static List<File> loopDirs(String path) {
        return loopDirs(file(path));
    }

    public static List<File> loopDirs(File file) {
        return loopDirs(file, null);
    }

    public static List<File> loopDirs(String path, FileFilter fileFilter) {
        return loopDirs(file(path), fileFilter);
    }

    public static List<File> loopDirs(File file, FileFilter fileFilter) {
        final List<File> fileList = new ArrayList<>();
        if (null == file || false == file.exists()) {
            return fileList;
        }

        if (file.isDirectory()) {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }

            final File[] subFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty(subFiles)) {
                for (File tmp : subFiles) {
                    fileList.addAll(loopDirs(tmp, fileFilter));
                }
            }
        }

        return fileList;
    }


    public static List<File> loopDirsAndFiles(String path) {
        return loopDirs(file(path));
    }

    public static List<File> loopDirsAndFiles(File file) {
        return loopDirs(file, null);
    }

    public static List<File> loopDirsAndFiles(String path, FileFilter fileFilter) {
        return loopDirs(file(path), fileFilter);
    }

    public static List<File> loopDirsAndFiles(File file, FileFilter fileFilter) {
        final List<File> fileList = new ArrayList<>();
        if (null == file || false == file.exists()) {
            return fileList;
        }

        if (file.isDirectory()) {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }

            final File[] subFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty(subFiles)) {
                for (File tmp : subFiles) {
                    fileList.addAll(loopDirsAndFiles(tmp, fileFilter));
                }
            }
        } else {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }
        }

        return fileList;
    }


    public static List<File> loopDirsByPattern(String rootPath, String pathPattern) {
        return loopDirsByPattern(file(rootPath), pathPattern);
    }

    public static List<File> loopDirsByPattern(String rootPath, List<String> pathPatterns) {
        return loopDirsByPattern(file(rootPath), pathPatterns);
    }

    public static List<File> loopDirsByPattern(File file, String pathPattern) {
        if (pathPattern.contains("|")) {
            return loopDirsByPattern(file, Arrays.asList(pathPattern.split("\\|")));
        }
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(dressPathPattern(pathPattern));
        List<File> files = FileUtil.loopDirs(file, pathname -> matcher.matches(Paths.get(pathname.toURI())));
        return files;
    }

    public static List<File> loopDirsByPattern(File file, List<String> pathPatterns) {
        List<PathMatcher> matchers = pathPatterns.stream()
                .map(FileUtil::dressPathPattern)
                .map(p -> FileSystems.getDefault().getPathMatcher(p))
                .collect(Collectors.toList());


        List<File> files = FileUtil.loopDirs(file, pathname -> {
            for (PathMatcher matcher : matchers) {
                if (matcher.matches(Paths.get(pathname.toURI()))) {
                    return true;
                }
            }
            return false;
        });
        return files;
    }


    public static List<File> loopFilesByPattern(String rootPath, String pathPattern) {
        return loopFilesByPattern(file(rootPath), pathPattern);
    }

    public static List<File> loopFilesByPattern(String rootPath, List<String> pathPatterns) {
        return loopFilesByPattern(file(rootPath), pathPatterns);
    }

    public static List<File> loopFilesByPattern(File file, String pathPattern) {
        if (pathPattern.contains("|")) {
            return loopFilesByPattern(file, Arrays.asList(pathPattern.split("\\|")));
        }
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(dressPathPattern(pathPattern));
        List<File> files = FileUtil.loopFiles(file, pathname -> matcher.matches(Paths.get(pathname.toURI())));
        return files;
    }

    public static List<File> loopFilesByPattern(File file, List<String> pathPatterns) {
        List<PathMatcher> matchers = pathPatterns.stream()
                .map(FileUtil::dressPathPattern)
                .map(p -> FileSystems.getDefault().getPathMatcher(p))
                .collect(Collectors.toList());


        List<File> files = FileUtil.loopFiles(file, pathname -> {
            for (PathMatcher matcher : matchers) {
                if (matcher.matches(Paths.get(pathname.toURI()))) {
                    return true;
                }
            }
            return false;
        });
        return files;
    }


    //////
    public static List<File> loopDirsAndFilesByPattern(String rootPath, String pathPattern) {
        return loopDirsAndFilesByPattern(file(rootPath), pathPattern);
    }

    public static List<File> loopDirsAndFilesByPattern(String rootPath, List<String> pathPatterns) {
        return loopDirsAndFilesByPattern(file(rootPath), pathPatterns);
    }

    public static List<File> loopDirsAndFilesByPattern(File file, String pathPattern) {
        if (pathPattern.contains("|")) {
            return loopDirsAndFilesByPattern(file, Arrays.asList(pathPattern.split("\\|")));
        }
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(dressPathPattern(pathPattern));
        List<File> files = FileUtil.loopDirsAndFiles(file, pathname -> {
            System.out.println(pathname.getAbsolutePath());
            return matcher.matches(Paths.get(pathname.toURI()));
        });
        return files;
    }

    public static List<File> loopDirsAndFilesByPattern(File file, List<String> pathPatterns) {
        List<PathMatcher> matchers = pathPatterns.stream()
                .map(FileUtil::dressPathPattern)
                .map(p -> FileSystems.getDefault().getPathMatcher(p))
                .collect(Collectors.toList());


        List<File> files = FileUtil.loopDirsAndFiles(file, pathname -> {
            for (PathMatcher matcher : matchers) {
                if (matcher.matches(Paths.get(pathname.toURI()))) {
                    return true;
                }
            }
            return false;
        });
        return files;
    }


    public static String dressPathPattern(String pathPattern) {
        if (!pathPattern.startsWith("glob:") && !pathPattern.startsWith("regex:")) {
            pathPattern = "glob:" + pathPattern;
        }
        return pathPattern;
    }

    public static String readAll(File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader utf8Reader = getUtf8Reader(file)) {
            String tmp;
            while ((tmp = utf8Reader.readLine()) != null) {
                sb.append(tmp).append("\n");
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return sb.toString();
    }

    public static void writeAll(String content, File file) {
        if (!exist(file)) {
            touch(file);
        }
        try (BufferedWriter writer = getWriter(file, StandardCharsets.UTF_8, false)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
