using System;
using System.Collections.Generic;

namespace ContaminaDOS_BackEnd.Models;

public partial class GameRound
{
    public string id { get; set; } = null!;

    public string leader { get; set; } = null!;

    public string round_status { get; set; } = null!;

    public string result { get; set; } = null!;

    public string phase { get; set; } = null!;

    public string game_id { get; set; } = null!;

    public string? createdAt { get; set; }

    public string? updatedAt { get; set; }

    public virtual ICollection<RoundGroup> RoundGroups { get; set; } = new List<RoundGroup>();

    public virtual Game game { get; set; } = null!;
}
