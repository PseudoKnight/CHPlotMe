/*
 * Copyright 2013 Jason Unger <entityreborn@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.entityreborn.chplotme;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHLog;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.LogLevel;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CDouble;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;
import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;
import com.worldcretornica.plotme.PlotMapInfo;
import com.worldcretornica.plotme.PlotMe;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;

/**
 *
 * @author Jason Unger <entityreborn@gmail.com>
 */
public class CHPlotMe {

    @api(environments = {CommandHelperEnvironment.class})
    public static class plot_owner extends AbstractFunction {

        public Exceptions.ExceptionType[] thrown() {
            return null;
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            String worldName = args[0].val();
            String id = args[1].val();
            
            if (!PlotManager.isValidId(id)) {
                throw new ConfigRuntimeException("Invalid id (" + id + ") for plot_owner", ExceptionType.RangeException, t);
            }
            
            if (!PlotManager.isPlotWorld(worldName)) {
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), ExceptionType.InvalidWorldException, t);
            }
            
            Plot plot = PlotManager.getPlotById(worldName, id);

            if (plot != null) {
                return new CString(plot.getOwner(), t);
            }

            return new CNull(t);
        }

        public String getName() {
            return getClass().getSimpleName();
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "string {worldname, id} Returns the owner of the plot of `id` in `worldname`.";
        }

        public CHVersion since() {
            return CHVersion.V3_3_1;
        }
    }
    
    @api(environments = {CommandHelperEnvironment.class})
    public static class plot_list extends AbstractFunction {
        
        public Exceptions.ExceptionType[] thrown() {
            return null;
        }
        
        public boolean isRestricted() {
            return true;
        }
        
        public Boolean runAsync() {
            return false;
        }
        
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            String worldName = args[0].val();
            
            if (!PlotManager.isPlotWorld(worldName)) {
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), ExceptionType.InvalidWorldException, t);
            }
            
            HashMap<String, Plot> plots = PlotManager.getPlots(worldName);
            
            CArray retn = new CArray(t);
            
            for (String id : plots.keySet()) {
                retn.push(new CString(id, t));
            }
            
            return retn;
        }
        
        public String getName() {
            return getClass().getSimpleName();
        }
        
        public Integer[] numArgs() {
            return new Integer[]{1};
        }
        
        public String docs() {
            return "array {world} Get the plot ids for a given world.";
        }
        
        public CHVersion since() {
            return CHVersion.V3_3_1;
        }
    }
    
    @api(environments = {CommandHelperEnvironment.class})
    public static class plotid_at_loc extends AbstractFunction {
        
        public Exceptions.ExceptionType[] thrown() {
            return null;
        }
        
        public boolean isRestricted() {
            return true;
        }
        
        public Boolean runAsync() {
            return false;
        }
        
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            MCLocation loc;
            
            if (args.length == 0) {
                MCPlayer p = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
                loc = p.getLocation();
            } else {
                if (args[0] instanceof CString) {
                    MCPlayer p = Static.GetPlayer(args[0].val(), t);
                    loc = p.getLocation();
                } else if (args[0] instanceof CArray) {
                    CArray arr = (CArray)args[0];
                    loc = ObjectGenerator.GetGenerator().location(arr, null, t);
                } else {
                    throw new ConfigRuntimeException("Invalid argument.", Exceptions.ExceptionType.FormatException, t);
                }
            }
            
            String id = PlotManager.getPlotId((Location)loc.getHandle());
            
            return new CString(id, t);
        }
        
        public String getName() {
            return getClass().getSimpleName();
        }
        
        public Integer[] numArgs() {
            return new Integer[]{1};
        }
        
        public String docs() {
            return "string {[location]} Get the plots id for a given location.";
        }
        
        public CHVersion since() {
            return CHVersion.V3_3_1;
        }
    }
    
    @api(environments = {CommandHelperEnvironment.class})
    public static class plot_info extends AbstractFunction {

        public Exceptions.ExceptionType[] thrown() {
            return null;
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            String id = args[1].val();
            String world = args[0].val();
            
            if (!PlotManager.isValidId(id)) {
                throw new ConfigRuntimeException("Invalid id (" + id + ") for plot_info", ExceptionType.RangeException, t);
            }
            
            if (!PlotManager.isPlotWorld(world)) {
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), ExceptionType.InvalidWorldException, t);
            }
            
            Plot plot = PlotManager.getPlotById(world, id);
            
            if (plot == null) {
                return new CNull(t);
            }
            
            CArray retn = new CArray(t);
            
            retn.set("id", plot.id);
            retn.set("world", plot.world);
            retn.set("owner", plot.getOwner());
            
            CArray allowed = new CArray(t);
            for (String allow : plot.allowed()) {
                allowed.push(new CString(allow, t));
            }
            
            retn.set("allowed", allowed, t);
            
            CArray denied = new CArray(t);
            for (String deny : plot.denied()) {
                allowed.push(new CString(deny, t));
            }
            
            retn.set("denied", denied, t);
            
            retn.set("biome", plot.biome.name());
            
            retn.set("finished", new CBoolean(plot.finished, t), t);
            retn.set("finishdate", plot.finisheddate);
            
            retn.set("forsale", new CBoolean(plot.forsale, t), t);
            retn.set("currentbidder", plot.currentbidder);
            retn.set("currentbid", new CDouble(plot.currentbid, t), t);
            retn.set("customprice", new CDouble(plot.customprice, t), t);
            
            retn.set("protect", new CBoolean(plot.protect, t), t);
            
            return retn;
        }

        public String getName() {
            return getClass().getSimpleName();
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "array {world, id} Return an array of information for a given plot id.";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }
    
    @api(environments = {CommandHelperEnvironment.class})
    public static class set_plot_info extends AbstractFunction {

        public Exceptions.ExceptionType[] thrown() {
            return null;
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            String world = args[0].val();
            String id = args[1].val();
            Construct a = args[2];
            
            if (!PlotManager.isValidId(id)) {
                throw new ConfigRuntimeException("Invalid id (" + id + ") for set_plot_info", ExceptionType.RangeException, t);
            }
            
            if (!(a instanceof CArray)) {
                throw new ConfigRuntimeException("Arg 3 of set_plot_info must be an associative array", ExceptionType.FormatException, t);
            }
            
            CArray array = (CArray)a;
            
            if (!array.inAssociativeMode()) {
                throw new ConfigRuntimeException("Arg 3 of set_plot_info must be an associative array", ExceptionType.FormatException, t);
            }
            
            if (!PlotManager.isPlotWorld(world)) {
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), ExceptionType.InvalidWorldException, t);
            }
            
            Plot plot = PlotManager.getPlotById(world, id);
            
            if (plot == null) {
                return new CNull(t);
            }
            
            for (String key : array.keySet()) {
                Construct data = array.get(key);
                
                if (key.equalsIgnoreCase("owner")) {
                    if (data instanceof CString) {
                        plot.owner = data.val();
                        plot.updateField("owner", data.val());
                        PlotManager.setOwnerSign(Bukkit.getWorld(world), plot);
                        
                        continue;
                    } else {
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's owner arg expects a string!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("biome")) {
                    if (data instanceof CString) {
                        try {
                            Biome biome = Biome.valueOf(data.val().toUpperCase());
                            PlotManager.setBiome(Bukkit.getWorld(world), 
                                    plot.id, plot, biome);
                        } catch (IllegalArgumentException e) {
                            CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's biome arg is invalid (" + data.val() + ")!", t);
                        }
                        
                        continue;
                    } else {
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's biome arg expects a string!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("finished")) {
                    if (data instanceof CBoolean) {
                        CBoolean bdata = (CBoolean)data;
                        
                        if (bdata.getBoolean()) {
                            plot.setFinished();
                        } else {
                            plot.setUnfinished();
                        }
                        
                        plot.updateField("finished", bdata.getBoolean());
                        
                        continue;
                    } else {
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's finished arg expects a boolean!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("allowed")) {
                    if (data instanceof CArray) {
                        CArray adata = (CArray)data;
                        
                        plot.removeAllAllowed();
                        
                        for (String allow : adata.keySet()) {
                            plot.addAllowed(allow);
                        }
                        
                        continue;
                    } else {
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's allowed arg expects an array!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("denied")) {
                    if (data instanceof CArray) {
                        CArray adata = (CArray)data;
                        
                        plot.removeAllDenied();
                        
                        for (String allow : adata.keySet()) {
                            plot.addDenied(allow);
                        }
                        
                        continue;
                    } else {
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's denied arg expects an array!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("forsale")) {
                    if (data instanceof CBoolean) {
                        CBoolean bdata = (CBoolean)data;
                        
                        plot.forsale = bdata.getBoolean();
                        plot.updateField("forsale", bdata.getBoolean());
                        PlotManager.setSellSign(Bukkit.getWorld(world), plot);
                        
                        continue;
                    } else {
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's forsale arg expects a boolean!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("currentbidder")) {
                    if (data instanceof CString) {
                        plot.currentbidder = data.val();
                        plot.updateField("currentbidder", data.val());
                        PlotManager.setSellSign(Bukkit.getWorld(world), plot);
                        
                        continue;
                    } else {
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's currentbidder arg expects a string!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("currentbid")) {
                    if (data instanceof CInt) {
                        CInt idata = (CInt)data;
                        plot.currentbid = idata.getInt();
                        plot.updateField("currentbid", idata.getInt());
                        PlotManager.setSellSign(Bukkit.getWorld(world), plot);
                        
                        continue;
                    } else if (data instanceof CDouble) {
                        CDouble ddata = (CDouble)data;
                        plot.currentbid = ddata.getDouble();
                        plot.updateField("currentbid", ddata.getDouble());
                        PlotManager.setSellSign(Bukkit.getWorld(world), plot);
                        
                        continue;
                    } else{
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's currentbid arg expects an integer or double!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("customprice")) {
                    if (data instanceof CInt) {
                        CInt idata = (CInt)data;
                        plot.customprice = idata.getInt();
                        plot.updateField("customprice", idata.getInt());
                        PlotManager.setSellSign(Bukkit.getWorld(world), plot);
                        
                        continue;
                    } else if (data instanceof CDouble) {
                        CDouble ddata = (CDouble)data;
                        plot.customprice = ddata.getDouble();
                        plot.updateField("customprice", ddata.getDouble());
                        PlotManager.setSellSign(Bukkit.getWorld(world), plot);
                        
                        continue;
                    } else{
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's currentbid arg expects an integer or double!", t);
                    }
                }
                
                if (key.equalsIgnoreCase("protect")) {
                    if (data instanceof CBoolean) {
                        CBoolean bdata = (CBoolean)data;
                        
                        plot.protect = bdata.getBoolean();
                        plot.updateField("protect", bdata.getBoolean());
                        
                        continue;
                    } else {
                        CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "set_plot_info's forsale arg expects a boolean!", t);
                    }
                }
                
                CHLog.GetLogger().Log(CHLog.Tags.RUNTIME, LogLevel.WARNING, 
                                "Unknown key '" + key + "' for set_plot_info, ignoring.", t);
            }
            
            return new CVoid(t);
        }

        public String getName() {
            return getClass().getSimpleName();
        }

        public Integer[] numArgs() {
            return new Integer[]{3};
        }

        public String docs() {
            return "void {world, id, array} Set information for a given plot id."
                    + " Almost anything returned by plot_info is settable.";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }
    
    @api(environments = {CommandHelperEnvironment.class})
    public static class plot_world_info extends AbstractFunction {

        public Exceptions.ExceptionType[] thrown() {
            return null;
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            String world = args[0].val();
            
            if (!PlotManager.isPlotWorld(world)) {
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), ExceptionType.InvalidWorldException, t);
            }
            
            CArray retn = new CArray(t);
            
            PlotMapInfo info = PlotManager.getMap(world);
            
            retn.set("plotsize", new CInt(info.PlotSize, t), t);
            retn.set("pathwidth", new CInt(info.PathWidth, t), t);
            
            CArray plots = new CArray(t);
            for (String plot : info.plots.keySet()) {
                plots.push(new CString(plot, t));
            }
            
            retn.set("plots", plots, t);
            
            return retn;
        }

        public String getName() {
            return getClass().getSimpleName();
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "array {worldname} Return an array of PlotMe world information.";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }
    
    @api(environments = {CommandHelperEnvironment.class})
    public static class player_plots extends AbstractFunction {

        public Exceptions.ExceptionType[] thrown() {
            return null;
        }

        public boolean isRestricted() {
            return true;
        }

        public Boolean runAsync() {
            return false;
        }

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            String world = args[0].val();
            String player = args[1].val();
            
            if (!PlotManager.isPlotWorld(world)) {
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), ExceptionType.InvalidWorldException, t);
            }
            
            PlotMapInfo info = PlotManager.getMap(world);
            
            CArray plots = new CArray(t);
            for (Plot plot : info.plots.values()) {
                if (player.equalsIgnoreCase(plot.owner)) {
                    plots.push(new CString(plot.id, t));
                }
            }
            
            return plots;
        }

        public String getName() {
            return getClass().getSimpleName();
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "array {worldname, player} Return an array of plots owned by a player in a given world.";
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }
}
