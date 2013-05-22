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
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CDouble;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions;
import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;
import com.worldcretornica.plotme.PlotMapInfo;
import com.worldcretornica.plotme.PlotMe;
import java.util.HashMap;
import org.bukkit.Location;

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
            
            if (!PlotManager.isPlotWorld(worldName)) {
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), t);
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
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), t);
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
            
            if (!PlotManager.isPlotWorld(world)) {
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), t);
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
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), t);
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
                throw new ConfigRuntimeException(PlotMe.caption("MsgNotPlotWorld"), t);
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
