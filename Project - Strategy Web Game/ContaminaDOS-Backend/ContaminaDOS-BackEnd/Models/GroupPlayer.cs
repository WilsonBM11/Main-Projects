using System;
using System.Collections.Generic;

namespace ContaminaDOS_BackEnd.Models;

public partial class GroupPlayer
{
    public string player_id { get; set; } = null!;

    public string group_id { get; set; } = null!;

    public virtual RoundGroup group { get; set; } = null!;

    public virtual Player player { get; set; } = null!;
}
