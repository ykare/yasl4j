/*
 * Copyright (c) 2021 Yoshiyuki Karezaki(y.karezaki@gmail.com)
 * Released under the MIT license
 * https://github.com/ykare/yasl4j/blob/master/LICENSE.txt
 */
package yasl4j.sound;

import org.junit.jupiter.api.Test;

class SoundPlayerTest {
    @Test
    public void testMp3() {
        SoundPlayer soundPlayer = new SoundPlayer("/sl_sound.mp3", SoundPlayer.MEDIA_TYPE.MP3, SoundPlayer.SOURCE_TYPE.RESOURCE);
        soundPlayer.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        soundPlayer.stop();
    }

    @Test()
    public void testWav() {
        SoundPlayer soundPlayer = new SoundPlayer(getClass().getResource("/sl_sound.wav").getFile(), SoundPlayer.MEDIA_TYPE.WAV, SoundPlayer.SOURCE_TYPE.FILE);
        soundPlayer.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        soundPlayer.stop();
    }
}