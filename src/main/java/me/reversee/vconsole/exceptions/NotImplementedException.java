package me.reversee.vconsole.exceptions;

import me.reversee.vconsole.util.Logger;

public class NotImplementedException extends Exception{
    public NotImplementedException() {
        Logger.log("Not implemented!", Logger.logfile, true);
    }

}
