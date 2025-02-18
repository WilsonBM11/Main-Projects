using System;
using System.Collections.Generic;
using ContaminaDOS_BackEnd.Models;
using Microsoft.EntityFrameworkCore;

namespace ContaminaDOS_BackEnd.Data;

public partial class DataContext : DbContext
{
    public DataContext()
    {
    }

    public DataContext(DbContextOptions<DataContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Game> Games { get; set; }

    public virtual DbSet<GameRound> GameRounds { get; set; }

    public virtual DbSet<GroupPlayer> GroupPlayers { get; set; }

    public virtual DbSet<Player> Players { get; set; }

    public virtual DbSet<RoundAction> RoundActions { get; set; }

    public virtual DbSet<RoundGroup> RoundGroups { get; set; }

    public virtual DbSet<Vote> Votes { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
        => optionsBuilder.UseSqlServer("Server=163.178.173.130;Database=ContaminaDOS-GRD;TrustServerCertificate=True;User Id=basesdedatos;Password=rpbases.2022");

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Game>(entity =>
        {
            entity.ToTable("Game");

            entity.HasIndex(e => e.game_name, "UK_Game").IsUnique();

            entity.Property(e => e.id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.createdAt)
                .HasMaxLength(24)
                .IsUnicode(false);
            entity.Property(e => e.currentRound)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.game_name)
                .HasMaxLength(20)
                .IsUnicode(false);
            entity.Property(e => e.game_owner)
                .HasMaxLength(20)
                .IsUnicode(false);
            entity.Property(e => e.game_password)
                .HasMaxLength(20)
                .IsUnicode(false);
            entity.Property(e => e.game_status)
                .HasMaxLength(6)
                .IsUnicode(false);
            entity.Property(e => e.updatedAt)
                .HasMaxLength(24)
                .IsUnicode(false);
        });

        modelBuilder.Entity<GameRound>(entity =>
        {
            entity.HasKey(e => e.id).HasName("PK_Round");

            entity.ToTable("GameRound");

            entity.Property(e => e.id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.createdAt)
                .HasMaxLength(24)
                .IsUnicode(false);
            entity.Property(e => e.game_id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.leader)
                .HasMaxLength(20)
                .IsUnicode(false);
            entity.Property(e => e.phase)
                .HasMaxLength(5)
                .IsUnicode(false);
            entity.Property(e => e.result)
                .HasMaxLength(8)
                .IsUnicode(false);
            entity.Property(e => e.round_status)
                .HasMaxLength(17)
                .IsUnicode(false);
            entity.Property(e => e.updatedAt)
                .HasMaxLength(24)
                .IsUnicode(false);

            entity.HasOne(d => d.game).WithMany(p => p.GameRounds)
                .HasForeignKey(d => d.game_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_Round");
        });

        modelBuilder.Entity<GroupPlayer>(entity =>
        {
            entity.HasKey(e => new { e.group_id, e.player_id });

            entity.ToTable("GroupPlayer");

            entity.Property(e => e.group_id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.player_id)
                .HasMaxLength(36)
                .IsUnicode(false);

            entity.HasOne(d => d.group).WithMany()
                .HasForeignKey(d => d.group_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_GP_GroupId");

            entity.HasOne(d => d.player).WithMany()
                .HasForeignKey(d => d.player_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_GP_PlayerId");
        });

        modelBuilder.Entity<Player>(entity =>
        {
            entity.ToTable("Player");

            entity.Property(e => e.id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.game_id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.player_name)
                .HasMaxLength(20)
                .IsUnicode(false);
            entity.Property(e => e.player_role)
                .HasMaxLength(7)
                .IsUnicode(false);

            entity.HasOne(d => d.game).WithMany(p => p.Players)
                .HasForeignKey(d => d.game_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_Player");
        });

        modelBuilder.Entity<RoundAction>(entity =>
        {
            entity.HasKey(e => new { e.round_id, e.player_id });

            entity.ToTable("RoundAction");

            entity.Property(e => e.player_id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.round_id)
                .HasMaxLength(36)
                .IsUnicode(false);

            entity.HasOne(d => d.player).WithMany()
                .HasForeignKey(d => d.player_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_A_PlayerId");

            entity.HasOne(d => d.round).WithMany()
                .HasForeignKey(d => d.round_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_A_RoundId");
        });

        modelBuilder.Entity<RoundGroup>(entity =>
        {
            entity.HasKey(e => e.id).HasName("PK_Group");

            entity.ToTable("RoundGroup");

            entity.Property(e => e.id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.phase)
                .HasMaxLength(5)
                .IsUnicode(false);
            entity.Property(e => e.round_id)
                .HasMaxLength(36)
                .IsUnicode(false);

            entity.HasOne(d => d.round).WithMany(p => p.RoundGroups)
                .HasForeignKey(d => d.round_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_Group");
        });

        modelBuilder.Entity<Vote>(entity =>
        {
            entity.HasKey(e => new { e.group_id, e.player_id });

            entity.ToTable("Vote");

            entity.Property(e => e.group_id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.player_id)
                .HasMaxLength(36)
                .IsUnicode(false);
            entity.Property(e => e.vote1).HasColumnName("vote");

            entity.HasOne(d => d.group).WithMany()
                .HasForeignKey(d => d.group_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_V_GroupId");

            entity.HasOne(d => d.player).WithMany()
                .HasForeignKey(d => d.player_id)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_V_PlayerId");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
