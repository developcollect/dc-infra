package com.developcollect.dcinfra.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


public class FileUtil extends cn.hutool.core.io.FileUtil {


    static final String specialS1 = "\0";
    static final String specialS2 = "/";
    static final String specialS3 = "\\";
    /**
     * doc的后缀
     */
    public static final String[] DOC_EXT = new String[]{"doc", "docx", "wps", "txt", "pdf"};
    /**
     * excel后缀
     */
    public static final String[] EXCEL_EXT = new String[]{"xlsx", "xlsm", "xltx", "xltm", "xlsb", "xlam"};
    /**
     * ppt后缀
     */
    public static final String[] PPT_EXT = new String[]{"ppt", "pptx", "pptm", "ppsx", "potx", "potm"};
    /**
     * 视频后缀
     */
    public static final String[] VIDEO_EXT = new String[]{"avi", "asf", "wmv", "avs", "flv", "mkv", "mov", "3gp",
            "mp4", "mpg", "mpeg", "dat", "ogm", "vob", "rmvb", "rm", "ts", "ifo"};
    /**
     * 音频后缀
     */
    public static final String[] AUDIO_EXT = new String[]{"wav", "aac", "mp3", "aif", "au", "ram", "wma", "amr"};
    /**
     * 压缩包后缀
     */
    public static final String[] ZIP_EXT = new String[]{"zip", "rar", "tar", "gz", "tar.gz", "7z", "gzip"};
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileUtil.class);

    private static Map<String, String> fileTypeMap = new HashMap<String, String>();
    private static Map<String, List<String>> whiteList = new HashMap<String, List<String>>();
    private static List<String> blackList = new ArrayList<String>();

    static {
        // 初始化文件类型信息
        initFileTypeHeadInfos();
    }


    /**
     * 根据文件大小转换为B、KB、MB、GB单位字符串显示
     *
     * @param fileSize 文件的大小,单位: byte
     * @return 返回 转换后带有单位的字符串
     */
    public static String humanSize(long fileSize) {
        String strFileSize;
        if (fileSize < 1024) {
            strFileSize = fileSize + "B";
        } else if (fileSize < 1024 * 1024) {
            strFileSize = Math.ceil(fileSize / 1024.0 * 100) / 100 + "KB";
        } else if (fileSize < 1024 * 1024 * 1024) {
            strFileSize = Math.ceil(fileSize / (1024 * 1024.0) * 100) / 100 + "MB";
        } else {
            strFileSize = Math.ceil(fileSize / (1024 * 1024 * 1024.0) * 100) / 100 + "GB";
        }
        return strFileSize;
    }

    public static String humanSize(File file) {
        return humanSize(size(file));
    }


    /**
     * 根据文件头特征获取文件的真实类型
     *
     * @param file 文件对象
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2019/11/23 14:30
     */
    public static String getRealType(File file) {
        BufferedInputStream inputStream = getInputStream(file);
        String fileSuffix = getRealType(inputStream);
        IoUtil.close(inputStream);
        return fileSuffix;
    }

    /**
     * 从输入流中读取前10个字节数据作为文件特征进行识别
     * 从而获取文件的真实类型
     * 当无法识别时返回空字符串
     *
     * @param in 输入流
     * @return java.lang.String
     * @author Zhu Kaixiao
     * @date 2020/10/20 15:36
     */
    public static String getRealType(InputStream in) {
        String fileHeaderCode = getFileHeaderCode(in);
        String fileSuffix = Optional.ofNullable(getFileSuffix(fileHeaderCode)).orElse("");
        return fileSuffix;
    }


    /**
     * 以指定的后缀名创建临时文件<br>
     * 创建后的文件名为 [Random].[suffix]
     * 创建的文件在 System.getProperty("java.io.tmpdir") 文件夹下
     *
     * @param suffix
     * @return java.io.File
     * @author Zhu Kaixiao
     * @date 2019/11/23 14:35
     */
    public static File createTempFile(String suffix) {
        File tempFile = createTempFileIn(System.getProperty("java.io.tmpdir"), suffix);
        return tempFile;
    }

    /**
     * 在指定目录下创建临时文件
     *
     * @param dir
     * @return java.io.File
     * @author Zhu Kaixiao
     * @date 2019/11/23 15:15
     */
    public static File createTempFileIn(String dir) {
        File tempFile = createTempFile("tempfile", null, new File(dir), true);
        return tempFile;
    }

    /**
     * 在指定目录下以指定后缀名创建临时文件
     *
     * @param dir
     * @param suffix
     * @return java.io.File
     * @author Zhu Kaixiao
     * @date 2019/11/23 15:17
     */
    public static File createTempFileIn(String dir, String suffix) {
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }
        File tempFile = createTempFile("tempfile", suffix, new File(dir), true);
        return tempFile;
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 [Random].tmp
     * 创建的文件在 System.getProperty("java.io.tmpdir") 文件夹下
     *
     * @return java.io.File
     * @author Zhu Kaixiao
     * @date 2019/11/23 14:35
     */
    public static File createTempFile() {
        File tempFile = createTempFileIn(FileUtil.getTmpDirPath(), ".tmp");
        return tempFile;
    }


    /**
     * 是否是文档
     *
     * @param ext 后缀格式
     */
    public static boolean isDoc(String ext) {
        ext = ext.toLowerCase(Locale.ENGLISH);
        for (String s : DOC_EXT) {
            if (s.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否Excel格式
     *
     * @param ext 格式
     * @Title: isValidExcelExt
     * @return: boolean true 是Excel
     */
    public static boolean isExcel(String ext) {
        ext = ext.toLowerCase(Locale.ENGLISH);
        for (String s : EXCEL_EXT) {
            if (s.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否ppt格式
     *
     * @param ext 格式
     * @Title: isValidPptExt
     * @return: boolean true 是ppt
     */
    public static boolean isPpt(String ext) {
        ext = ext.toLowerCase(Locale.ENGLISH);
        for (String s : PPT_EXT) {
            if (s.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否视频格式
     *
     * @param ext 格式
     * @Title: isValidVideoExt
     * @return: boolean true 是视频
     */
    public static boolean isVideo(String ext) {
        ext = ext.toLowerCase(Locale.ENGLISH);
        for (String s : VIDEO_EXT) {
            if (s.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否音频格式
     *
     * @param ext 格式
     * @Title: isValidAudioExt
     * @return: boolean true 是音频
     */
    public static boolean isAudio(String ext) {
        ext = ext.toLowerCase(Locale.ENGLISH);
        for (String s : AUDIO_EXT) {
            if (s.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否压缩包格式
     *
     * @param ext 格式
     * @Title: isValidZipExt
     * @return: boolean true 是压缩包
     */
    public static boolean isZip(String ext) {
        ext = ext.toLowerCase(Locale.ENGLISH);
        for (String s : ZIP_EXT) {
            if (s.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 输入流转字节输出流
     *
     * @param inputStream InputStream
     * @Title: convertInputStreamToByte
     * @return: ByteArrayOutputStream
     */
    public static ByteArrayOutputStream convertInputStreamToByte(InputStream inputStream) {
        try {
            ByteArrayOutputStream outByte = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                outByte.write(buffer, 0, len);
            }
            outByte.flush();
            return outByte;
        } catch (Exception e) {
            log.debug("文件流转换出错{}", e.getMessage());
            return null;
        }
    }

    /**
     * 格式化文件名或者路徑名
     *
     * @param filename 文件名或者路径名
     * @Title: normalizeFilename
     * @return: String
     */
    public static String normalizeFilename(String filename) {
        if (StringUtils.isBlank(filename)) {
            return filename;
        }
        return java.text.Normalizer.normalize(filename, java.text.Normalizer.Form.NFKD);
    }

    private static void initFileTypeHeadInfos() {
        List<String> jpgHeadList = new ArrayList<String>();
        jpgHeadList.add("ffd8ff");
        List<String> pngHeadList = new ArrayList<String>();
        pngHeadList.add("89504e");
        List<String> gifHeadList = new ArrayList<String>();
        gifHeadList.add("47494638");
        List<String> bmpHeadList = new ArrayList<String>();
        bmpHeadList.add("424d");
        List<String> aviHeadList = new ArrayList<String>();
        aviHeadList.add("41564920");
        aviHeadList.add("52494646");
        List<String> wmvHeadList = new ArrayList<String>();
        wmvHeadList.add("3026b2758e66cf11a6d9");
        List<String> mp4HeadList = new ArrayList<String>();
        mp4HeadList.add("000000");
        List<String> pdfHeadList = new ArrayList<String>();
        pdfHeadList.add("255044462d312e");
        List<String> docHeadList = new ArrayList<String>();
        docHeadList.add("d0cf11e0a1b11ae10000");
        List<String> docxHeadList = new ArrayList<String>();
        docxHeadList.add("504b03041400");
        List<String> htmlHeadList = new ArrayList<String>();
        htmlHeadList.add("68746D6C3E");
        htmlHeadList.add("3c21444f435459504520");
        List<String> flvHeadList = new ArrayList<String>();
        flvHeadList.add("464c56");
        List<String> tifHeadList = new ArrayList<String>();
        tifHeadList.add("49492A00");
        List<String> rarHeadList = new ArrayList<String>();
        rarHeadList.add("52617221");
        rarHeadList.add("1f8b0800");
        rarHeadList.add("504b0304140000000800");

        whiteList.put("jpg", jpgHeadList);
        whiteList.put("jpeg", jpgHeadList);
        whiteList.put("png", pngHeadList);
        whiteList.put("gif", gifHeadList);
        whiteList.put("bmp", bmpHeadList);
        whiteList.put("avi", aviHeadList);
        whiteList.put("wmv", wmvHeadList);
        whiteList.put("mp4", mp4HeadList);
        whiteList.put("pdf", pdfHeadList);
        whiteList.put("doc", docHeadList);
        whiteList.put("xls", docHeadList);
        whiteList.put("ppt", docHeadList);
        whiteList.put("flv", flvHeadList);
        whiteList.put("docx", docxHeadList);
        whiteList.put("xlsx", docxHeadList);
        whiteList.put("pptx", docxHeadList);
        whiteList.put("html", htmlHeadList);
        whiteList.put("tif", tifHeadList);
        whiteList.put("rar", rarHeadList);
        // txt 文件头过于多了
        whiteList.put("txt", null);
        whiteList.put("3gp", null);
        blackList.add("4d5a90");

        fileTypeMap.put("ffd8ff", "jpg");
        fileTypeMap.put("89504e", "png");
        fileTypeMap.put("47494638", "gif");
        fileTypeMap.put("424d", "bmp");
        fileTypeMap.put("41564920", "avi");
        fileTypeMap.put("52494646", "avi");
        fileTypeMap.put("3026b2758e66cf11a6d9", "wmv");
        fileTypeMap.put("000000", "mp4");
        fileTypeMap.put("255044462d312e", "pdf");
        fileTypeMap.put("d0cf11e0a1b11ae10000", "doc");
        // docx|xlsx|pptx
        fileTypeMap.put("504b03041400", "docx");
        fileTypeMap.put("68746D6C3E", "html");
        fileTypeMap.put("3c21444f435459504520", "html");
        fileTypeMap.put("3c21646f637479706520", "htm");
        fileTypeMap.put("49492a", "tif");
        fileTypeMap.put("414331", "dwg");
        fileTypeMap.put("48544d", "css");
        fileTypeMap.put("696b2e", "js");
        fileTypeMap.put("7b5c72", "rtf");
        fileTypeMap.put("384250", "psd");
        fileTypeMap.put("46726f", "eml");
        fileTypeMap.put("537461", "mdb");
        fileTypeMap.put("252150", "ps");
        fileTypeMap.put("2e524d", "rmvb");
        // flv、f4v
        fileTypeMap.put("464c56", "flv");
        fileTypeMap.put("494433", "mp3");
        fileTypeMap.put("000001", "mpg");
        fileTypeMap.put("52494646e27807005741", "wav");
        // MIDI (mid)
        fileTypeMap.put("4d546864000000060001", "mid");
        fileTypeMap.put("504b0304140000000800", "zip");
        fileTypeMap.put("52617221", "rar");
        fileTypeMap.put("235468", "ini");
        fileTypeMap.put("504b03040a0000000000", "jar");
        fileTypeMap.put("4d5a90", "exe");
        fileTypeMap.put("3c2540", "jsp");
        fileTypeMap.put("4d616e", "mf");
        fileTypeMap.put("3C3F786D6C", "xml");
        fileTypeMap.put("494e53", "sql");
        fileTypeMap.put("706163", "java");
        fileTypeMap.put("406563", "bat");
        fileTypeMap.put("1f8b0800000000000000", "gz");
        fileTypeMap.put("6c6f67", "properties");
        fileTypeMap.put("cafeba", "class");
        fileTypeMap.put("495453", "chm");
        fileTypeMap.put("04000000010000001300", "mxp");
        fileTypeMap.put("6431303a637265617465", "torrent");
        fileTypeMap.put("6D6F6F76", "mov");
        fileTypeMap.put("FF575043", "wpd");
        fileTypeMap.put("CFAD12FEC5FD746F", "dbx");
        fileTypeMap.put("2142444E", "pst");
        fileTypeMap.put("AC9EBD8F", "qdf");
        fileTypeMap.put("E3828596", "pwl");
        fileTypeMap.put("2E7261FD", "ram");
        fileTypeMap.put("49492A00", "tif");
    }

    /**
     * 二进制数组转十六进制
     *
     * @param src 文件数组
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null == src || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取文件真实类型
     *
     * @param fileHeaderCode 文件头
     * @return
     */
    private static String getFileSuffix(String fileHeaderCode) {
        for (Map.Entry<String, String> entry : fileTypeMap.entrySet()) {
            String key = entry.getKey();

            boolean match = key.length() > fileHeaderCode.length()
                    ? key.toLowerCase().startsWith(fileHeaderCode.toLowerCase())
                    : fileHeaderCode.toLowerCase().startsWith(key.toLowerCase());

            if (match) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * 获取文件头
     *
     * @param in 輸入流
     * @Title: getFileHeaderCode
     * @return: String
     */
    private static String getFileHeaderCode(InputStream in) {
        byte[] b = new byte[10];
        String fileCode = "";
        try {
            in.read(b, 0, b.length);
            fileCode = bytesToHexString(b);
        } catch (IOException e) {
            log.error("IOException:", e);
        }
        return fileCode;
    }


    /**
     * 修改文件的后缀为该文件的真实类型
     *
     * @param file
     * @return java.io.File
     * @author Zhu Kaixiao
     * @date 2019/11/23 15:03
     */
    public static File renameToRealType(File file) {
        String newSuffix = "." + FileUtil.getRealType(file);
        String canonicalPath = FileUtil.getCanonicalPath(file);
        if (canonicalPath.endsWith(newSuffix)) {
            return file;
        }
        String newPath = StringUtils.substringBeforeLast(canonicalPath, ".") + newSuffix;
        rename(file, newPath, false, true);
        return new File(newPath);
    }


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


    /**
     * 同步源文件到目标文件
     * 或者 同步源文件夹到目标文件夹
     *
     * @param source
     * @param target
     * @author Zhu Kaixiao
     * @date 2020/10/27 10:05
     */
    public static void sync(File source, File target) {
        if (source.isDirectory() && target.isDirectory()) {
            Set<String> sourceFiles = Sets.newHashSet(source.list());
            Set<String> targetFiles = Sets.newHashSet(target.list());

            // remove files from target that are not in source
            for (String targetFile : targetFiles) {
                if (!sourceFiles.contains(targetFile)) {
                    del(new File(target, targetFile));
                }
            }

            for (String sourceFile : sourceFiles) {
                File file = new File(source, sourceFile);
                File file2 = new File(target, sourceFile);
                if (file.isFile()) {
                    copyIfChanged(file, file2);
                } else {
                    file2.mkdir();
                    sync(file, file2);
                }
            }
        } else if (source.isFile() && target.isFile()) {
            copyIfChanged(source, target);
        }
    }

    /**
     * 复制源文件到目标文件，如果目标文件不存在，直接复制
     * 如果目标文件相对源文件发生变动，用源文件覆盖，否则不做任何操作
     *
     * @param source
     * @param target
     * @author Zhu Kaixiao
     * @date 2020/10/27 10:02
     */
    private static void copyIfChanged(File source, File target) {
        if (target.exists()) {
            if (source.length() == target.length() && checksumCRC32(source) == checksumCRC32(target)) {
                return;
            } else {
                target.delete();
            }
        }
        if (!source.renameTo(target)) {
            move(source, target, true);
        }
    }

}
