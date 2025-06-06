<?php

namespace App\Entity\GsProjet;

use App\Repository\GsProjet\MissionRepository;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\DBAL\Types\Types;
use App\Entity\GsProjet\Project;
use App\Entity\User; // Ajout du bon namespace pour User

#[ORM\Entity(repositoryClass: MissionRepository::class)]
#[ORM\Table(name: "missions")]
#[ORM\HasLifecycleCallbacks]
class Mission
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 100)]
    private ?string $titre = null;

    #[ORM\Column(type: Types::TEXT, nullable: true)]
    private ?string $description = null;

    #[ORM\Column(length: 20)]
    private ?string $status = 'To Do';

    #[ORM\ManyToOne(targetEntity: Project::class, inversedBy: 'missions')]
#[ORM\JoinColumn(nullable: false, onDelete: "CASCADE")]
private ?Project $project = null;
    #[ORM\ManyToOne(inversedBy: 'missions')]
    

    #[ORM\Column(type: Types::DATETIME_MUTABLE)]
    private ?\DateTimeInterface $createdAt = null;

    #[ORM\Column(type: Types::DATETIME_MUTABLE)]
    private ?\DateTimeInterface $updatedAt = null;
    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: 'created_by_id', referencedColumnName: 'id')]
    private ?User $createdBy = null;
    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: 'assigned_to', referencedColumnName: 'id')]
    private ?User $assignedTo = null;
    #[ORM\Column(type: Types::DATETIME_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $dateTerminer = null;
    #[ORM\Column(type: 'boolean')]
private bool $notifiedLate = false;
    public function __construct()
    {
        $this->createdAt = new \DateTime();
        $this->updatedAt = new \DateTime();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getTitre(): ?string
    {
        return $this->titre;
    }

    public function setTitre(string $titre): static
    {
        $this->titre = $titre;
        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): static
    {
        $this->description = $description;
        return $this;
    }

    public function getStatus(): ?string
    {
        return $this->status;
    }

    public function setStatus(string $status): static
    {
        $this->status = $status;
        return $this;
    }

    public function getProject(): ?Project
    {
        return $this->project;
    }

    public function setProject(?Project $project): static
    {
        $this->project = $project;
        return $this;
    }

    public function getAssignedTo(): ?User
    {
        return $this->assignedTo;
    }

    public function setAssignedTo(?User $assignedTo): static
    {
        $this->assignedTo = $assignedTo;
        return $this;
    }

    public function getCreatedAt(): ?\DateTimeInterface
    {
        return $this->createdAt;
    }

    public function getUpdatedAt(): ?\DateTimeInterface
    {
        return $this->updatedAt;
    }

    public function getDateTerminer(): ?\DateTimeInterface
    {
        return $this->dateTerminer;
    }

    public function setDateTerminer(?\DateTimeInterface $dateTerminer): static
    {
        $this->dateTerminer = $dateTerminer;
        return $this;
    }
  
  
    public function setUpdatedAt(\DateTimeInterface $updatedAt): static
    {
        $this->updatedAt = $updatedAt;
        return $this;
    }
    #[ORM\PrePersist]
    #[ORM\PreUpdate]
    public function updateTimestamps(): void
    {
        $this->updatedAt = new \DateTime();
    }
    public function getCreatedBy(): ?User
    {
        return $this->createdBy;
    }

    public function setCreatedBy(?User $createdBy): self
    {
        $this->createdBy = $createdBy;
        return $this;
    }
    // Dans Mission.php
// Dans App\Entity\GsProjet\Mission

public function getDaysLate(): int
{
    if (!$this->dateTerminer || $this->status === 'Done') {
        return 0;
    }
    
    $today = new \DateTime();
    if ($this->dateTerminer > $today) {
        return 0;
    }
    
    return $today->diff($this->dateTerminer)->days;
}
// Dans votre entité Mission.php
public function getProjectTitle(): string
{
    return $this->project ? $this->project->getNom() : 'Aucun projet associé';
}
// src/Entity/GsProjet/Mission.php



public function isNotifiedLate(): bool
{
    return $this->notifiedLate;
}

public function setNotifiedLate(bool $notifiedLate): self
{
    $this->notifiedLate = $notifiedLate;
    return $this;
}
#[ORM\Column(type: 'string', length: 255, nullable: true)]
private $meetLink;

// ... autres propriétés et méthodes ...

public function getMeetLink(): ?string
{
    return $this->meetLink;
}

public function setMeetLink(?string $meetLink): self
{
    $this->meetLink = $meetLink;
    return $this;
}
}
