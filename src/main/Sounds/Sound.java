package main.Sounds;
import java.net.URL;

import javax.sound.sampled.*;
public class Sound
{
    private static Sound instance;
    private Clip clip;
    private URL soundURL[] = new URL[30];
    private Clip introClip;
    private Clip gamePlayClip;

    private float volumeIntroSound = -10f;
    private float volumeGamePlaySound = -15f;
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
    private void setFileForLoopSound(int i)
    {
        AudioInputStream ais;
        try {
            if(i==5)
            {
                ais = AudioSystem.getAudioInputStream(soundURL[5]);
                introClip = AudioSystem.getClip();
                introClip.open(ais);
            }
            else if(i==6)
            {
                ais = AudioSystem.getAudioInputStream(soundURL[6]);
                gamePlayClip = AudioSystem.getClip();
                gamePlayClip.open(ais);
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void playIntroSound()
    {
        stopAllLoopSounds();
        setFileForLoopSound(5);
        FloatControl gainControl = (FloatControl) introClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volumeIntroSound); 
        introClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void playGamePlaySound()
    {
        stopAllLoopSounds();
        setFileForLoopSound(6);
        FloatControl gainControl = (FloatControl) gamePlayClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volumeGamePlaySound);
        gamePlayClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    private void stopAllLoopSounds()
    {
        stopIntroSound();
        stopGamePlaySound();
    }
    private void stopIntroSound()
    {
        if(introClip==null){return;}
        introClip.stop();
    }
    private void stopGamePlaySound()
    {
        if(gamePlayClip==null){return;}
        gamePlayClip.stop();
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