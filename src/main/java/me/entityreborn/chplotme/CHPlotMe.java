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

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.constructs.CArray;
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
import com.worldcretornica.plotme.PlotMe;
import java.util.HashMap;

/**
 *
 * @author Jason Unger <entityreborn@gmail.com>
 */
public class CHPlotMe {

    @api(environments = {CommandHelperEnvironment.class})
    public static class get_plot_owner extends AbstractFunction {

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

            throw new ConfigRuntimeException(PlotMe.caption("MsgNoPlotFound"), t);
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
    public static class get_plots extends AbstractFunction {
        
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
}
