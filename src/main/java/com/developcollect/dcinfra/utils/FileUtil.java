package com.developcollect.dcinfra.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class FileUtil extends cn.hutool.core.io.FileUtil {


    // region -------------------------------- loopDirs --------------------------------

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
        return loopDirs(file, fileFilter, 0);
    }

    private static List<File> loopDirs(File file, FileFilter fileFilter, int deep) {
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
                    fileList.addAll(loopDirs(tmp, fileFilter, ++deep));
                }
            }
        }

        return fileList;
    }

    // endregion


    // region -------------------------------- loopDirsAndFiles --------------------------------

    public static List<File> loopDirsAndFiles(String path) {
        return loopDirsAndFiles(file(path));
    }

    public static List<File> loopDirsAndFiles(File file) {
        return loopDirsAndFiles(file, null);
    }

    public static List<File> loopDirsAndFiles(String path, FileFilter fileFilter) {
        return loopDirsAndFiles(file(path), fileFilter);
    }

    public static List<File> loopDirsAndFiles(File file, FileFilter fileFilter) {
        return loopDirsAndFiles(file, fileFilter, 0);
    }


    private static List<File> loopDirsAndFiles(File file, FileFilter fileFilter, int deep) {
        final List<File> fileList = new ArrayList<>();
        if (null == file || false == file.exists()) {
            return fileList;
        }

        if (file.isDirectory()) {
            if (deep > 0 && (null == fileFilter || fileFilter.accept(file))) {
                fileList.add(file);
            }

            final File[] subFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty(subFiles)) {
                for (File tmp : subFiles) {
                    fileList.addAll(loopDirsAndFiles(tmp, fileFilter, ++deep));
                }
            }
        } else {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    // endregion


    // region -------------------------------- loopDirsByPattern --------------------------------

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

    // endregion


    // region -------------------------------- loopFilesByPattern --------------------------------

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

    // endregion


    // region -------------------------------- loopDirsAndFilesByPattern --------------------------------

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

    // endregion


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


    public static boolean hasDir(File dir) {
        if (dir == null || dir.isFile() || isDirEmpty(dir)) {
            return false;
        }
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir.toPath(), entry -> entry.toFile().isDirectory());
            return directoryStream.iterator().hasNext();
        } catch (IOException e) {
            return LambdaUtil.doThrow(e);
        }
    }

    public static boolean hasFile(File dir) {
        if (dir == null || dir.isFile() || isDirEmpty(dir)) {
            return false;
        }
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir.toPath(), entry -> entry.toFile().isFile());
            return directoryStream.iterator().hasNext();
        } catch (IOException e) {
            return LambdaUtil.doThrow(e);
        }
    }

    /**
     * 列出指定文件夹下的所有文件夹，不会递归列出
     * <p>
     * 相当于  ls -l | grep ^d 命令
     *
     * @param dir
     * @return java.util.List<java.io.File>
     */
    public static List<File> lsDirs(File dir) {
        return lsDirs(dir, f -> true);
    }

    public static List<File> lsDirs(File dir, FileFilter fileFilter) {
        return lsAll(dir, f -> f.isDirectory() && fileFilter.accept(f));
    }

    public static List<File> lsFiles(File dir) {
        return lsFiles(dir, f -> true);
    }


    public static List<File> lsFiles(File dir, FileFilter fileFilter) {
        return lsAll(dir, f -> f.isFile() && fileFilter.accept(f));
    }

    public static List<File> lsAll(File dir, FileFilter fileFilter) {
        if (dir == null || !dir.isDirectory()) {
            return Collections.emptyList();
        }
        File[] files = dir.listFiles();
        if (ArrayUtil.isEmpty(files)) {
            return Collections.emptyList();
        }

        List<File> dirs = Arrays.stream(files)
                .filter(fileFilter::accept)
                .collect(Collectors.toList());
        return dirs;
    }

    public static List<File> lsAll(File dir) {
        return lsAll(dir, f -> true);
    }


    /**
     * 获取子文件从父文件夹起始的路径
     *
     * @param parent
     * @param child
     * @return java.lang.String
     */
    public static String relaPath(File parent, File child) {
        String parentAbsolutePath = parent.getAbsolutePath();
        return child.getAbsolutePath().substring(parentAbsolutePath.length());
    }

}
