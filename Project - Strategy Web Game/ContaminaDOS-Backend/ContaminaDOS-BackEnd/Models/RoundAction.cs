using System;
using System.Collections.Generic;

namespace ContaminaDOS_BackEnd.Models;

public partial class RoundAction
{
    public string round_id { get; set; } = null!;

    public string player_id { get; set; } = null!;

    public bool round_action { get; set; }

    public virtual Player player { get; set; } = null!;

    public virtual GameRound round { get; set; } = null!;
}
