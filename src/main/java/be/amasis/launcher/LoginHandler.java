package be.amasis.launcher;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.exception.BadServerResponseException;
import fr.theshark34.supdate.exception.BadServerVersionException;
import fr.theshark34.supdate.exception.ServerDisabledException;
import fr.theshark34.supdate.exception.ServerMissingSomethingException;
import sun.security.util.AuthResources;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class LoginHandler {
    private static String ip = "http://149.91.80.185/launcher/s-update";
    private static GameInfos infos = new GameInfos(StartHandler.NAME, new GameVersion("1.12", GameType.V1_8_HIGHER), new GameTweak[]{GameTweak.FORGE});
    private static File dir = infos.getGameDir();
    private static File crashDir = new File(dir, "errors");
    private static AuthInfos authInfos;
    private static Thread update;
    private static CrashReporter reporter = new CrashReporter(StartHandler.NAME, crashDir);

    public static void auth(String user, String pwd) throws AuthenticationException {
        Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
        AuthResponse authResponse = authenticator.authenticate(AuthAgent.MINECRAFT, user, pwd, "");
        authInfos = new AuthInfos(authResponse.getSelectedProfile().getName(), authResponse.getAccessToken(), authResponse.getSelectedProfile().getId());
    }

    public static void update() throws BadServerResponseException, IOException, BadServerVersionException, ServerDisabledException, ServerMissingSomethingException {
        SUpdate server = new SUpdate(ip, dir);
        server.getServerRequester().setRewriteEnabled(true);
        update = new Thread(){
            private int val,max;
            public void run(){
                while(!isInterrupted()){
                    if(BarAPI.getNumberOfFileToDownload() == 0){
                        StartHandler.getFrame().getPanel().setInfoText("Vérification des fichiers...");
                    }else{
                        val = BarAPI.getNumberOfDownloadedFiles();
                        max = BarAPI.getNumberOfFileToDownload();
                        StartHandler.getFrame().getPanel().getProgressBar().setValue((int) BarAPI.getNumberOfTotalDownloadedBytes());
                        StartHandler.getFrame().getPanel().getProgressBar().setMaximum((int) BarAPI.getNumberOfTotalBytesToDownload());
                        if(val > max){
                            StartHandler.getFrame().getPanel().setInfoText("Téléchargement des fichiers terminés.");
                        }else{
                            StartHandler.getFrame().getPanel().setInfoText("Téléchargement en cours: "+val+"/"+max);
                        }

                    }
                }
            }
        };
        update.start();
        server.start();
        update.interrupt();
    }

    public static void launch() throws LaunchException {
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, new GameFolder("game/assets", "game/libs", "game/natives", "game/minecraft.jar"), authInfos);
        profile.getVmArgs().addAll(Arrays.asList("-Xms512M", "-Xmx2048M"));
        //profile.getVmArgs().addAll(Arrays.asList(StartHandler.getFrame().getPanel().getRam()));
        profile.setRedirectErrorStream(true);
        ExternalLauncher launcher = new ExternalLauncher(profile);
        launcher.setLogsEnabled(true);
        StartHandler.getFrame().setVisible(false);
        try{
            launcher.launch();
        }catch (LaunchException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void interruptUpdate(){
        update.interrupt();
    }

    public static CrashReporter getReporter() {
        return reporter;
    }
}
