package main.Sounds;
import java.net.URL;

import javax.sound.sampled.*;
public class Sound
{
    private static Sound instance;
    private Clip clip;
    private URL soundURL[] = new URL[30];
    private Sound() 
    {
        String path = "/Assets/Sounds/";
        soundURL[0] = getClass().getResource(path+"sound_clicked_button.wav");
        soundURL[1] = getClass().getResource(path+"write-paper.wav");
        soundURL[2] = getClass().getResource(path+"coin_receive.wav");
        soundURL[3] = getClass().getResource(path+"game_over.wav");
        soundURL[4] = getClass().getResource(path+"success.wav");
        soundURL[5] = getClass().getResource(path+"menu_intro.wav");
        soundURL[6] = getClass().getResource(path+"gameplay_sound.wav");
    }

    public void setFile(int i)
    {
        AudioInputStream ais;
        try {
            ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playSound()
    {
        clip.start();
    }
    public void stopSound()
    {
        clip.stop();
    }
    public void loopSound()
    {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public static Sound Instance()
    {
        if(instance==null)
        {
            instance = new Sound();
        }
        return instance;
    }

}