using System;
using System.Collections.Generic;

namespace ContaminaDOS_BackEnd.Models;

public partial class Game
{
    public string id { get; set; } = null!;

    public string game_name { get; set; } = null!;

    public string game_owner { get; set; } = null!;

    public string game_status { get; set; } = null!;

    public bool use_password { get; set; }

    public string game_password { get; set; } = null!;

    public string currentRound { get; set; } = null!;

    public string? createdAt { get; set; }

    public string? updatedAt { get; set; }

    public virtual ICollection<GameRound> GameRounds { get; set; } = new List<GameRound>();

    public virtual ICollection<Player> Players { get; set; } = new List<Player>();
}
