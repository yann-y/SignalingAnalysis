package pojo;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class CustomWaterMark implements AssignerWithPunctuatedWatermarks<String> {

    public static final long serialVersionUID = 2L;

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(String s, long l) {
        String t = s.split(",")[0];
        if(t.length()>1)
        {
            return new Watermark(Long.parseLong(t)*1000);
        }
        else
        {
            return null;
        }

    }

    @Override
    public long extractTimestamp(String s, long l) {
        String t = s.split(",")[0];
        if(t.length()>1)
        {
            return Long.parseLong(t)*1000;
        }
        else
        {
            return 0;
        }

    }
}
