package com.szcgc.cougua.utils;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

//isoparser 有bug 视频的分割与合并也可以用其他方法例如：ffmpeg,MediaCodec 等
public class Mp4ParserUtils {

    private static final Logger logger = LoggerFactory.getLogger(Mp4ParserUtils.class);

    public static void main(String[] args) {
        //一个拆分多个
//        String filePath="D:\\tools\\douyinVideo\\画江湖之不良人\\1\\333.mp4";//视频路径
//        String workingPath="D:\\tools\\douyinVideo\\画江湖之不良人\\1\\";//输出路径
//        String outName="42.mp4";//输出文件名
//        double startTime= 20;//剪切起始时间 > 0.03  秒
//        double endTime = 120;//剪切结束时间 秒
//        clipMp4Video(filePath,workingPath,outName,startTime,endTime);
        //多个合并一个
//        List<String> filePaths = new ArrayList<>(4);
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\1.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\2.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\3.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\4.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\5.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\6.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\7.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\4.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\90.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\113.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\ywh\\1\\102.mp4");
//        mergeVideoNew(filePaths, new File("D:\\tools\\douyinVideo\\ywh\\1\\222.mp4"));

        //给视频加字幕
        addSubtitles("D:\\tools\\douyinVideo\\ywh\\1\\1.mp4","D:\\tools\\douyinVideo\\ywh\\1\\11.mp4");
    }

    //注意合并文件的位置顺序 保证需要合并的视频文件是相同高度的视频文件
    public static String mergeVideoNew(List<String> filePaths, File resultFile) {
        try {
            Collections.sort(filePaths);
//            logger.info("filePaths=" + JSON.toJSONString(filePaths));
            Movie[] inMovies = new Movie[filePaths.size()];
            for (int i = 0; i < filePaths.size(); i++) {
                logger.info("filePaths=" + filePaths.get(i));
                File f = new File(filePaths.get(i));
                if (f.exists() && f.isFile() && f.length() > 0) {
                    logger.info("filePaths real:" + f.getAbsolutePath());
                    inMovies[i] = MovieCreator.build(filePaths.get(i));
                }
            }
            // 分别取出音轨和视频
            List<Track> videoTracks = new LinkedList<>();
            List<Track> audioTracks = new LinkedList<>();
            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if ("soun".equals(t.getHandler())) {
                        audioTracks.add(t);
                    }
                    if ("vide".equals(t.getHandler())) {
                        videoTracks.add(t);
                    }
                }
            }
            // 合并到最终的视频文件
            Movie outMovie = new Movie();
            if (audioTracks.size() > 0) {
                outMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                outMovie.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            }
            Container mp4file = new DefaultMp4Builder().build(outMovie);
            // 将文件输出
            if (resultFile.exists() && resultFile.isFile()) {
                resultFile.delete();
            }
            FileChannel fc = new RandomAccessFile(resultFile, "rw").getChannel();
            mp4file.writeContainer(fc);
            fc.close();
            // 合成完成后把原片段文件删除
            for (String filePath : filePaths) {
                File file = new File(filePath);
                file.delete();
            }
            return resultFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            logger.error("mergeVideo has error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("mergeVideo has error1: " + e.getMessage());
        }
        return null;
    }

    /**
     * 对 Mp4 添加字幕
     *
     * @param mp4Path .mp4 添加字幕之前
     * @param outPath .mp4 添加字幕之后
     */
    public static void addSubtitles(String mp4Path, String outPath){
        try {
            Movie videoMovie = MovieCreator.build(mp4Path);
            TextTrackImpl subTitleEng = new TextTrackImpl();// 实例化文本通道对象
            subTitleEng.getTrackMetaData().setLanguage("eng");// 设置元数据(语言)
            subTitleEng.getSubs().add(new TextTrackImpl.Line(0, 1000, "wwwwwwwwwwwwwwwwwwwwwwwwwww"));// 参数时间毫秒值
            subTitleEng.getSubs().add(new TextTrackImpl.Line(1000, 2000, "111111111111111111111111111111"));
            subTitleEng.getSubs().add(new TextTrackImpl.Line(2000, 3000, "22222222222222222222222222"));
            subTitleEng.getSubs().add(new TextTrackImpl.Line(3000, 4000, "3333333333333333333333333"));
            subTitleEng.getSubs().add(new TextTrackImpl.Line(4000, 5000, "4444444444444444444444444"));
            subTitleEng.getSubs().add(new TextTrackImpl.Line(5001, 5002, "5555555555555"));// 省略去测试
            videoMovie.addTrack(subTitleEng);// 将字幕通道添加进视频Movie对象中
            Container out = new DefaultMp4Builder().build(videoMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 切割视频
     * @param filePath //视频路径
     * @param workingPath //输出路径
     * @param outName 切后文件名
     * @param startTime //剪切起始时间
     * @param endTime //剪切结束时间
     */
    public static void clipMp4Video(String filePath,String workingPath,String outName,double startTime,double endTime){
        try {
            Movie movie = MovieCreator.build(filePath);
            List<Track> tracks = movie.getTracks();
            movie.setTracks(new LinkedList<Track>());
            //移除旧的通道
            boolean timeCorrected = false;
            //计算剪切时间
            for (Track track : tracks) {
                if (track.getSyncSamples() != null
                        && track.getSyncSamples().length > 0) {
                    if (timeCorrected) {
                        throw new RuntimeException(
                                "The startTime has already been corrected by another track with SyncSample. Not Supported.");
                    }
                    startTime = correctTimeToSyncSample(track, startTime, false);
                    endTime = correctTimeToSyncSample(track, endTime, true);
                    timeCorrected = true;
                }
            }
            for (Track track : tracks) {
                long currentSample = 0;
                double currentTime = 0;
                double lastTime = 0;
                long startSample1 = -1;
                long endSample1 = -1;
                for (int i = 0; i < track.getSampleDurations().length; i++) {
                    long delta = track.getSampleDurations()[i];
                    if (currentTime > lastTime && currentTime <= startTime) {
                        startSample1 = currentSample;
                    }
                    if (currentTime > lastTime && currentTime <= endTime) {
                        endSample1 = currentSample;
                    }
                    lastTime = currentTime;
                    currentTime += (double) delta
                            / (double) track.getTrackMetaData().getTimescale();
                    currentSample++;
                }
                movie.addTrack(new CroppedTrack(track, startSample1, endSample1));// new
            }
            //合成视频mp4
            Container out = new DefaultMp4Builder().build(movie);
            File storagePath = new File(workingPath);
            storagePath.mkdirs();
            FileOutputStream fos = new FileOutputStream(new File(storagePath, outName));
            FileChannel fco = fos.getChannel();
            out.writeContainer(fco);
            fco.close();
            fos.close();
            System.out.println("end...............");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //换算剪切时间
    private static double correctTimeToSyncSample(Track track, double cutHere,
                                                 boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];
            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(),
                        currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta
                    / (double) track.getTrackMetaData().getTimescale();
            currentSample++;
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }



    /**
     * 对AAC文件集合进行追加合并(按照顺序一个一个拼接起来)
     *
     * @param aacPathList [输入]AAC文件路径的集合(不支持wav)
     * @param outPutPath  [输出]结果文件全部名称包含后缀(比如.aac)
     * @throws IOException 格式不支持等情况抛出异常
     */
    public static void appendAacList(List<String> aacPathList, String outPutPath){
        try{
            List<Track> audioTracks = new LinkedList<>();// 音频通道集合
            for (int i = 0; i < aacPathList.size(); i++) {// 将每个文件路径都构建成一个AACTrackImpl对象
                audioTracks.add(new AACTrackImpl(new FileDataSourceImpl(aacPathList.get(i))));
            }
            Movie resultMovie = new Movie();// 结果Movie对象[输出]
            if (!audioTracks.isEmpty()) {// 将所有音频通道追加合并
                resultMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }
            Container outContainer = new DefaultMp4Builder().build(resultMovie);// 将结果Movie对象封装进容器
            FileChannel fileChannel = new RandomAccessFile(String.format(outPutPath), "rw").getChannel();
            outContainer.writeContainer(fileChannel);// 将容器内容写入磁盘
            fileChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将 AAC 和 MP4 进行混合[替换了视频的音轨]
     *
     * @param aacPath .aac
     * @param mp4Path .mp4
     * @param outPath .mp4
     */
    public static boolean muxAacMp4(String aacPath, String mp4Path, String outPath) {
        boolean flag=false;
        try {
            AACTrackImpl aacTrack = new AACTrackImpl(new FileDataSourceImpl(aacPath));
            Movie videoMovie = MovieCreator.build(mp4Path);
            Track videoTracks = null;// 获取视频的单纯视频部分
            for (Track videoMovieTrack : videoMovie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks = videoMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks);// 视频部分
            resultMovie.addTrack(aacTrack);// 音频部分
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }

    /**
     * 将 M4A 和 MP4 进行混合[替换了视频的音轨]
     *
     * @param m4aPath .m4a[同样可以使用.mp4]
     * @param mp4Path .mp4
     * @param outPath .mp4
     */
    public static void muxM4AMp4(String m4aPath, String mp4Path, String outPath) throws IOException {
        Movie audioMovie = MovieCreator.build(m4aPath);
        Track audioTracks = null;// 获取视频的单纯音频部分
        for (Track audioMovieTrack : audioMovie.getTracks()) {
            if ("soun".equals(audioMovieTrack.getHandler())) {
                audioTracks = audioMovieTrack;
            }
        }
        Movie videoMovie = MovieCreator.build(mp4Path);
        Track videoTracks = null;// 获取视频的单纯视频部分
        for (Track videoMovieTrack : videoMovie.getTracks()) {
            if ("vide".equals(videoMovieTrack.getHandler())) {
                videoTracks = videoMovieTrack;
            }
        }
        Movie resultMovie = new Movie();
        resultMovie.addTrack(videoTracks);// 视频部分
        resultMovie.addTrack(audioTracks);// 音频部分
        Container out = new DefaultMp4Builder().build(resultMovie);
        FileOutputStream fos = new FileOutputStream(new File(outPath));
        out.writeContainer(fos.getChannel());
        fos.close();
    }

    /**
     * 分离mp4视频的音频部分，只保留视频部分
     *
     * @param mp4Path .mp4
     * @param outPath .mp4
     */
    public static void splitMp4(String mp4Path, String outPath){
        try{
            Movie videoMovie = MovieCreator.build(mp4Path);
            Track videoTracks = null;// 获取视频的单纯视频部分
            for (Track videoMovieTrack : videoMovie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks = videoMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks);// 视频部分
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 分离mp4的视频部分，只保留音频部分
     *
     * @param mp4Path .mp4
     * @param outPath .aac
     */
    public static void splitAac(String mp4Path, String outPath){
        try{
            Movie videoMovie = MovieCreator.build(mp4Path);
            Track videoTracks = null;// 获取音频的单纯视频部分
            for (Track videoMovieTrack : videoMovie.getTracks()) {
                if ("soun".equals(videoMovieTrack.getHandler())) {
                    videoTracks = videoMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks);// 音频部分
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 分离mp4视频的音频部分，只保留视频部分
     *
     * @param mp4Path .mp4
     * @param mp4OutPath  mp4视频输出路径
     * @param aacOutPath  aac视频输出路径
     */
    public static void splitVideo(String mp4Path, String mp4OutPath,String aacOutPath){
        try{
            Movie videoMovie = MovieCreator.build(mp4Path);
            Track videTracks = null;// 获取视频的单纯视频部分
            Track sounTracks = null;// 获取视频的单纯音频部分
            for (Track videoMovieTrack : videoMovie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videTracks = videoMovieTrack;
                }
                if ("soun".equals(videoMovieTrack.getHandler())) {
                    sounTracks = videoMovieTrack;
                }
            }
            Movie videMovie = new Movie();
            videMovie.addTrack(videTracks);// 视频部分
            Movie sounMovie = new Movie();
            sounMovie.addTrack(sounTracks);// 音频部分
            // 视频部分
            Container videout = new DefaultMp4Builder().build(videMovie);
            FileOutputStream videfos = new FileOutputStream(new File(mp4OutPath));
            videout.writeContainer(videfos.getChannel());
            videfos.close();
            // 音频部分
            Container sounout = new DefaultMp4Builder().build(sounMovie);
            FileOutputStream sounfos = new FileOutputStream(new File(aacOutPath));
            sounout.writeContainer(sounfos.getChannel());
            sounfos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将 MP4 切割
     *
     * @param mp4Path    .mp4
     * @param fromSample 起始位置   不是传入的秒数
     * @param toSample   结束位置   不是传入的秒数
     * @param outPath    .mp4
     */
    public static void cropMp4(String mp4Path, long fromSample, long toSample, String outPath){
        try{
            Movie mp4Movie = MovieCreator.build(mp4Path);
            Track videoTracks = null;// 获取视频的单纯视频部分
            for (Track videoMovieTrack : mp4Movie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks = videoMovieTrack;
                }
            }
            Track audioTracks = null;// 获取视频的单纯音频部分
            for (Track audioMovieTrack : mp4Movie.getTracks()) {
                if ("soun".equals(audioMovieTrack.getHandler())) {
                    audioTracks = audioMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(new AppendTrack(new CroppedTrack(videoTracks, fromSample, toSample)));// 视频部分
            resultMovie.addTrack(new AppendTrack(new CroppedTrack(audioTracks, fromSample, toSample)));// 音频部分
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
