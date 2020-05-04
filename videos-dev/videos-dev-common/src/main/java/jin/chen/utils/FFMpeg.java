package jin.chen.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpeg {

    private  String ffmpeg;

    public FFMpeg(String ffmpeg) {
        this.ffmpeg = ffmpeg;
    }

    public  void pruduct(String inVideo, String inMusic, double time, String out){
        /*
        ffmpeg转换视频格式  windows环境执行命令
        ffmpeg.exe -i in  out
         */
        /*
        ffmpeg合并视频音频  windows环境执行命令,分别输入视频音频地址，时间，输出地址
        ffmpeg.exe -i invideo -i inmusic -t time -y out
         */
        /*
        ffmpeg合并视频音频  windows环境执行命令,
        上面的那种命令可能某些合成不生效，视频还是没有背景音乐，换用下面这种
        ffmpeg.exe -i invideo -i inmusic -t time -filter_complex amix=inputs=2 out
         */
        List<String> command = new ArrayList<>();
        command.add(ffmpeg);
        command.add("-i");
        command.add(inVideo);
        command.add("-i");
        command.add(inMusic);
        command.add("-t");
        command.add(String.valueOf(time));
//        command.add("-y");
        command.add("-filter_complex");

        command.add("amix=inputs=2");
        command.add(out);
        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        /*
        因为ffmpeg软件在处理音视频过程中会产生很多临时文件，流，大量占用cpu内存，阻塞主线程
        这里读取释放一下
         */
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();
            inputStream = process.getErrorStream();
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);
            while((br.readLine()) != null){
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

                try{
                    if(br != null){
                        br.close();
                    }
                    if(isr != null){
                        br.close();
                    }
                    if(inputStream != null){
                        inputStream.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


        }
    }

    public  void pruductCover(String inVideo, String out){
        /*
        ffmpeg处理视频，截图某一帧数 windows环境执行命令,
        ffmpeg.exe -ss 00:00:01 -y -i video.mp4 -vframes 1 out.jpg
         */
        List<String> command = new ArrayList<>();
        command.add(ffmpeg);
        command.add("-ss");
        command.add("00:00:01");
        command.add("-y");
        command.add("-i");
        command.add(inVideo);
        command.add("-vframes");
        command.add("1");
        command.add(out);
        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        /*
        因为ffmpeg软件在处理音视频过程中会产生很多临时文件，流，大量占用cpu内存，阻塞主线程
        这里读取释放一下
         */
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();
            inputStream = process.getErrorStream();
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);
            while((br.readLine()) != null){
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            try{
                if(br != null){
                    br.close();
                }
                if(isr != null){
                    br.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FFMpeg ffMpeg = new FFMpeg("E:\\毕设\\项目必须软件\\windows\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe");
        ffMpeg.pruduct("D:\\asd.mp4", "D:\\金玟岐 - 岁月神偷.mp3", 7, "D:\\hhhh.mp4");
        ffMpeg.pruductCover("D:\\asd.mp4", "D:\\tupian.jpg");
    }
}
