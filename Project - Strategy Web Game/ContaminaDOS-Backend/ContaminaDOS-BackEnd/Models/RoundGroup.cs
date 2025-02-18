using System;
using System.Collections.Generic;

namespace ContaminaDOS_BackEnd.Models;

public partial class RoundGroup
{
    public string id { get; set; } = null!;

    public string phase { get; set; } = null!;

    public string round_id { get; set; } = null!;

    public virtual GameRound round { get; set; } = null!;
}
