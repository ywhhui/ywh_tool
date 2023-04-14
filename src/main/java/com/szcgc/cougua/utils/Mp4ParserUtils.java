package com.szcgc.cougua.utils;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//isoparser有bug 视频的分割与合并也可以用其他方法例如：ffmpeg,MediaCodec等
public class Mp4ParserUtils {

    private static final Logger logger = LoggerFactory.getLogger(Mp4ParserUtils.class);

    public static void main(String[] args) {
        //一个拆分多个
        String filePath="D:\\tools\\douyinVideo\\画江湖之不良人\\1\\333.mp4";//视频路径
        String workingPath="D:\\tools\\douyinVideo\\画江湖之不良人\\1\\";//输出路径
        String outName="4.mp4";//输出文件名
        double startTime= 1;//剪切起始时间 > 0.03  秒
        double endTime = 20;//剪切结束时间
        clipMp4Video(filePath,workingPath,outName,startTime,endTime);
        //多个合并一个
//        List<String> filePaths = new ArrayList<>(2);
//        filePaths.add("D:\\tools\\douyinVideo\\1\\21.mp4");
//        filePaths.add("D:\\tools\\douyinVideo\\1\\2.mp4");
//        mergeVideoNew(filePaths, new File("D:\\tools\\douyinVideo\\1\\333.mp4"));
    }

    //注意合并文件的位置顺序
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

}
