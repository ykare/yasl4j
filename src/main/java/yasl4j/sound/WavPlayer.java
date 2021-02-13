/*
 * Copyright (c) 2021 Yoshiyuki Karezaki(y.karezaki@gmail.com)
 * Released under the MIT license
 * https://github.com/ykare/yasl4j/blob/master/LICENSE.txt
 */
package yasl4j.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class WavPlayer implements SoundPlayer.Player {
    private static final int BUFFER_SIZE = 1024;
    private final byte[] buf = new byte[BUFFER_SIZE];

    @Override
    public void play(InputStream inputStream) {
        throw new UnsupportedOperationException("WavPlayer play(InputStream inputStream) not supported.");
    }

    @Override
    public void play(File wavFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream wav = AudioSystem.getAudioInputStream(wavFile);
        AudioFormat format = wav.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format, BUFFER_SIZE);
        line.start();
        int len;
        while ((len = wav.read(buf)) != -1)
            line.write(buf, 0, len);
        line.drain();
        line.stop();
        line.close();
    }

}