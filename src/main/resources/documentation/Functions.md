###Table of Contents###
* [`plot_owner`][plotowner]
* [`plot_list`][plotlist]
* [`plotid_at_loc`][plotidatloc]
* [`plot_info`][plotinfo]
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

<a id="plotworldinfo"></a>`plot_world_info(@world)` - *Return an array of information for a given plot id.*

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

  * `id` - [`@id`][id]
  * `world` - World this plot resides on.
  * `owner` - Owning player's name.
  * `allowed` - List of player names allowed to build on this plot, besides the owner.
  * `denied` - List of player names not allowed to build on this plot.
  * `biome` - Biome type for this plot.
  * `finished` - Boolean value of if this plot is flagged as finished.
  * `finisheddate` - Date the plot was finished on.
  * `forsale` - Boolean value of if this plot is for sale.
  * `currentbidder` - Playername of current bidder for this plot.
  * `currentbid` - Current bidder's bid.
  * `customprice` - Price for plot.
  * `protect` - Whether this plot is protected.
[pinfo]: #pinfo

* <a id="pwinfo"></a>`plotworldinfo` - Assoc. Array - *Information about a world:*

  * `plotsize` - Dimention of plots.
  * `pathwidth` - Width of paths between plots.
  * `plots` - Array of plot ids for plots in this world.
[pwinfo]: #pwinfo

