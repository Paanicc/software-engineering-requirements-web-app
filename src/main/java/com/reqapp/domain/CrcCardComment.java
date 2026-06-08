package com.reqapp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "crc_card_comments")
public class CrcCardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crc_card_id", nullable = false)
    private CrcCard crcCard;

    public CrcCardComment() {
    }

    public CrcCardComment(String text, User author, CrcCard crcCard) {
        this.text = text;
        this.author = author;
        this.crcCard = crcCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public CrcCard getCrcCard() {
        return crcCard;
    }

    public void setCrcCard(CrcCard crcCard) {
        this.crcCard = crcCard;
    }
}
