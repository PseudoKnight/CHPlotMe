###Table of Contents###
* [`plot_owner`][plotowner]
* [`plot_list`][plotlist]
* [`plotid_at_loc`][plotidatloc]
* [`plot_info`][plotinfo]
* [`set_plot_info`][setplotinfo]
* [`plot_world_info`][plotworldinfo]
* [`player_plots`][playerplots]

###General Functions###

<a id="plotowner"></a>`plot_owner(@world, `[`@id`][id]`)` - *Get the owner of a given plot.*

*Returns:* player name.

[plotowner]: #plotowner

---

<a id="plotlist"></a>`plot_list(@world)` - *Get a list of all the known plot ids for a given world.*

*Returns:* array of [`@id`][id]

[plotlist]: #plotlist

---

<a id="plotidatloc"></a>`plotid_at_loc(@location)` - *Get a plot's id for a given location.*

*Returns:* [`@id`][id]

[plotidatloc]: #plotidatloc

---

<a id="plotinfo"></a>`plot_info(@world, `[`@id`][id]`)` - *Return an array of information for a given plot id.*

*Returns:* [`@plotinfo`][pinfo]

[plotinfo]: #plotinfo

---

<a id="setplotinfo"></a>`set_plot_info(@world, `[`@id`][id]`, @array)` - *Sets information for a given plot id.* 

Almost anything returned by `plot_info` can be modified. *Experimental!*

*Returns:* void

[setplotinfo]: #setplotinfo

---

<a id="plotworldinfo"></a>`plot_world_info(@world)` - *Return an array of information for a given world.*

*Returns:* [`@plotworldinfo`][pwinfo]

[plotworldinfo]: #plotworldinfo

---

<a id="playerplots"></a>`player_plots(@world, @player)` - *Return an array of plots `player` owns in `world`.*

*Returns:* array of [`@ids`][id]

[playerplots]: #playerplots

---

###Glossary###

* <a id="id"></a>`id` - String - *The id of a plot, in format `x;y`.*
[id]: #id

* <a id="pinfo"></a>`plotinfo` - Assoc. Array - *Information about a given plot:*

  * `id` - String - [`@id`][id]
  * `world` - String - *World this plot resides on.*
  * `owner` - String - *Owning player's name.*
  * `allowed` - Array of String - *List of player names allowed to build on this plot, besides the owner.*
  * `denied` - Array of String - *List of player names not allowed to build on this plot.*
  * `biome` - String - *Biome type for this plot.*
  * `finished` - Boolean - *If this plot is flagged as finished.*
  * `finisheddate` - String - *Date the plot was finished on.*
  * `forsale` - Boolean - *If this plot is for sale.*
  * `currentbidder` - String - *Playername of current bidder for this plot.*
  * `currentbid` - Integer - *Current bidder's bid.*
  * `customprice` - Integer - *Price for plot.*
  * `protect` - Boolean - *Whether this plot is protected.*
[pinfo]: #pinfo

* <a id="pwinfo"></a>`plotworldinfo` - Assoc. Array - *Information about a world:*

  * `plotsize` - Integer - *Dimention of plots.*
  * `pathwidth` - Integer - *Width of paths between plots.*
  * `plots` - Array of String - *Plot* [`@ids`][id] *for plots in this world.*
[pwinfo]: #pwinfo