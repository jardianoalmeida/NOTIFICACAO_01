/*
 *
 *  Copyright (c) 2018-2020 Givantha Kalansuriya, This source is a part of
 *   Staxrt - sample application source code.
 *   http://staxrt.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.jardiano.notificacao.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "tb_notificacao")
@EntityListeners(AuditingEntityListener.class)
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "corpo", nullable = false)
    private String corpo;

  public long getId() {
        return id;
    }

  public void setId(long id) {
        this.id = id;
    }

  public String getTitulo() {
        return titulo;
    }

  public void setTitulo(String firstName) {
        this.titulo = firstName;
    }

  public String getCorpo() {
        return corpo;
    }

  public void setCorpo(String lastName) {
        this.corpo = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + titulo + '\'' +
                ", lastName='" + corpo + '\'' +
                '}';
    }

}
