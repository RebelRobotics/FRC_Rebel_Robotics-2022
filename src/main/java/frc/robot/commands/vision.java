// Copyrights (c) 2018-2019 FIRST, 2020 Highlanders FRC. All Rights Reserved.
package frc.robot.commands;

import edu.wpi.first.wpilibj.SerialPort;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class vision {
    
    public String debugString;
    private JSONParser parser = new JSONParser();
    private SerialPort port;
    private double lastParseTime;
    private double distance;
    private double angle;
    private boolean TRK;
    private boolean haveCamera;
    private AtomicBoolean shouldStop = new AtomicBoolean(false);
    private ConcurrentLinkedQueue<JSONObject> jsonResults = new ConcurrentLinkedQueue<JSONObject>();
    private ConcurrentLinkedQueue<String> stringResults = new ConcurrentLinkedQueue<String>();

    public vision(SerialPort jevois) {
        port = jevois;
        
        Runnable task =
                () -> {
                    String buffer = "";
                    while (!shouldStop.get()) {
                        if (port.getBytesReceived() > 0) {
                            String temp = port.readString();
                            buffer += temp;
                            stringResults.add(temp);
                        }
                        debugString = buffer;
                        // Consume bytes until the '{'
                        if (buffer.length() > 0) {
                            int index = buffer.indexOf('{', 0);
                            if (index != -1 && index != 0) {
                                buffer = buffer.substring(index);
                            } else if (index == -1) {
                                buffer = "";
                            }
                        }
                        // Search for '}' and parse JSON
                        if (buffer.length() > 0) {
                            int index = buffer.indexOf('}', 0);
                            if (index != -1) {
                                String section = buffer.substring(0, index + 1);
                                try {
                                    JSONObject json = (JSONObject) parser.parse(section);
                                    if (jsonResults.size() > 16) {
                                        jsonResults.poll();
                                    }
                                    jsonResults.add(json);
                                } catch (ParseException e) {
                                    System.err.println(e);
                                }
                                buffer = buffer.substring(index + 1);
                            }
                        }
                    }
                };
        Thread thread = new Thread(task);
        thread.start();
    }

    public boolean hasCamera(){
        if(port.getBytesReceived() > 0) { 
            haveCamera = true;
        }
        else {
            haveCamera = false;
        }
        return haveCamera;
    }


    public void updateVision() {
        // Drain existing objects out of queue and use most recent
        JSONObject json = jsonResults.poll();
        int jsonSize = jsonResults.size();
        for (int i = 0; i < jsonSize; i++) {
            JSONObject temp = jsonResults.poll();
            if (temp != null) {
                json = temp;
            }
        }


        // Use JSON results if present
        if (json != null) {
            
            Object tempDistance = json.get("Dist");
            
            if (tempDistance != null) {
                distance = Double.parseDouble(tempDistance.toString());
            } else {
                distance = 0;
            }

            Object tempAngle = json.get("DegToCtr");
            if (tempAngle != null) {
                angle = Double.parseDouble(tempAngle.toString());
            } else {
                angle = 0;
            }
            Object tempTrk = json.get("Trk");
            if (tempTrk!=null) {
                int TrkInt = Integer.parseInt(tempTrk.toString());
                if (TrkInt==0) {TRK=false;}
                if (TrkInt==1) {TRK=true;}
            }
        }
    }

    public boolean getTracking() {
        return TRK;
    }
    
    public double getDistance() {
        return distance;
    }

    public double getAngle() {
        return angle;
    }

    public double getLastParseTime() {
        return lastParseTime;
    }

    public double getCorrectedDistance(double a, double b, double c, double d) {
        updateVision();
        return a * Math.pow(getDistance(), 3)
                + b * Math.pow(getDistance(), 2)
                + c * getDistance()
                + d;
    }

}
