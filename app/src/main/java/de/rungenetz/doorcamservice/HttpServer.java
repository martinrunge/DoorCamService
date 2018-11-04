package de.rungenetz.doorcamservice;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.PowerManager;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.ContextCompat.startActivity;

public class HttpServer extends NanoHTTPD {

    private Context ctx = null;

    public HttpServer(int port, Context ctx) throws IOException {
        super(port);
        this.ctx = ctx;
        start();
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        HttpServer.LOG.info(method + " '" + uri + "' ");
        Map<String,List<String>> parms = session.getParameters();

        String pkg;

        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        String resp = "<html><body><h1>response from DoorCamService</h1><div>\n";

        if (uri.equals("/open")) {
            List<String> args = parms.get("package");
            if (args == null) {
                resp += "no package specified to open. Please add a query parameter 'package=com.example.app' to the request\n";
            } else {
                pkg = args.get(0);
                resp += "package = " + pkg + "\n";

                Intent i = ctx.getPackageManager().getLaunchIntentForPackage(pkg);
                if ( i != null) {
                    ctx.startActivity(i);

                    PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP, "CHESS");
                    wl.acquire();
                    try {
                        Thread.sleep(30 * 1000); // 30 seconds
                    } catch (Exception e) {
                    } finally {
                        wl.release();
                    }

                }
                else {
                    resp += "Error: package '" + pkg + "' not found or does not contain an Activity\n";
                }
            }

        }
        if (uri.equals("/close")) {
            List<String> args = parms.get("package");
            if (args == null) {
                resp += "no package specified to open. Please add a query parameter 'package=com.example.app' to the request\n";
            } else {
                pkg = args.get(0);
                resp += "package = " + pkg + "\n";
            }
        }
        if (uri.equals("/homescreen")) {
                resp += "going back to homescreen\n";

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(startMain);
        }



        // Intent intents = new Intent(ctx, hello.class);
        // intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // ctx.startActivity(intents);

        resp += "</div></body></html>\n";

        return Response.newFixedLengthResponse(resp);
    }
}
