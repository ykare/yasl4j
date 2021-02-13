/*
 * Copyright (c) 2021 Yoshiyuki Karezaki(y.karezaki@gmail.com)
 * Released under the MIT license
 * https://github.com/ykare/yasl4j/blob/master/LICENSE.txt
 */
package yasl4j.sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.InputStream;

public class Mp3Player implements SoundPlayer.Player {

    @Override
    public void play(InputStream inputStream) throws JavaLayerException {

        AudioDevice device = FactoryRegistry.systemRegistry().createAudioDevice();
        AdvancedPlayer player = new AdvancedPlayer(inputStream, device);
        player.play();
    }

    @Override
    public void play(File file) {
        throw new UnsupportedOperationException("Mp3Player play(File file) not supported.");
    }

}