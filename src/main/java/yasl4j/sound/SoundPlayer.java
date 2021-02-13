/*
 * Copyright (c) 2021 Yoshiyuki Karezaki(y.karezaki@gmail.com)
 * Released under the MIT license
 * https://github.com/ykare/yasl4j/blob/master/LICENSE.txt
 */
package yasl4j.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SoundPlayer {
    private final MEDIA_TYPE mediaType;
    private final SOURCE_TYPE sourceType;
    private final String source;
    private final Player player;
    private final Thread thread;
    private boolean shouldStop;

    public enum MEDIA_TYPE {MP3, WAV}

    public enum SOURCE_TYPE {FILE, RESOURCE}

    public SoundPlayer(String source, MEDIA_TYPE mediaType, SOURCE_TYPE sourceType) throws IllegalArgumentException {
        this.source = source;
        this.mediaType = mediaType;
        this.sourceType = sourceType;
        this.player = getPlayer(mediaType);
        this.thread = getThread();
    }

    public void start() {
        this.thread.start();
    }

    public void stop() {
        this.shouldStop = true;
    }

    private Player getPlayer(MEDIA_TYPE mediaType) {
        final Player result;
        switch (mediaType) {
            case MP3:
                result = new Mp3Player();
                break;
            case WAV:
                result = new WavPlayer();
                break;
            default:
                throw new IllegalArgumentException("Unsupported mediaType: " + mediaType);
        }
        return result;
    }

    private Thread getThread() {
        final Thread result;
        result = new Thread(() -> {
            while (!shouldStop) {
                try {
                    playSound();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        result.setDaemon(true);
        return result;
    }

    private void playSound() throws Exception {
        switch (mediaType) {
            case MP3:
                InputStream in;
                if (sourceType == SOURCE_TYPE.FILE) {
                    in = new FileInputStream(source);
                } else {
                    in = getClass().getResourceAsStream(source);
                }
                player.play(in);
                break;
            case WAV:
                if (sourceType == SOURCE_TYPE.FILE) {
                    player.play(new File(source));
                } else {
                    player.play(getClass().getResourceAsStream(source));
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported mediaType: " + mediaType);
        }
    }

    interface Player {
        void play(InputStream inputStream) throws Exception;

        void play(File file) throws Exception;
    }
}
