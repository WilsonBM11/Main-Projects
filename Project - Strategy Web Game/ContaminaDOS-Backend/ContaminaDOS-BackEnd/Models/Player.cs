using System;
using System.Collections.Generic;

namespace ContaminaDOS_BackEnd.Models;

public partial class Player
{
    public string id { get; set; } = null!;

    public string player_name { get; set; } = null!;

    public string game_id { get; set; } = null!;

    public string player_role { get; set; } = null!;

    public virtual Game game { get; set; } = null!;
}
