package com.example.atanke.lectura.models;

import java.util.ArrayList;
import java.util.List;
public class titulos {
        public String foto;
        public String titulo;
        public String descripcion;
        public int categoria;
        public int id;
        public String portada;
        public String publico;
        public String fecha;
        private List<titulos> listaCuento;
        private List<titulos> listaLeyenda;
        private List<titulos> listaMito;
        private List<titulos> listaTradiciones;
        private List<titulos> todo;

        public titulos() {
            this.listaCuento = new ArrayList<>();
            this.listaLeyenda = new ArrayList<>();
            this.listaMito = new ArrayList<>();
            this.listaTradiciones = new ArrayList<>();
            this.todo = new ArrayList<>();
        }
        public  titulos( String foto, String titulo, String descripcion, int categoria){
            if(foto!=null){
                this.foto=foto;
            }
            this.titulo=titulo;
            this.descripcion=descripcion;
            this.categoria=categoria;
        }
        // Getters y setters para los atributos foto, titulo y descripcion

        // Métodos para agregar elementos a las listas
        public void agregarCuento(titulos cuento) {
            listaCuento.add(cuento);
            todo.add(cuento);
        }
        public void agregarLeyenda(titulos leyenda) {
            listaLeyenda.add(leyenda);
            todo.add(leyenda);
        }
        public void agregarMito(titulos mito) {
            listaMito.add(mito);
            todo.add(mito);
        }
        public  void agregarTradiciones(titulos tradiciones){
            listaTradiciones.add(tradiciones);
            todo.add(tradiciones);
        }
        // Métodos para obtener las listas
        public List<titulos> getListaCuento() {
            return listaCuento;
        }
        public List<titulos> getListaLeyenda() {
            return listaLeyenda;
        }
        public List<titulos> getListaTradiciones() {
            return listaTradiciones;
        }
        public List<titulos> getListaMito() {
            return listaMito;
        }
        public List<titulos> getListaTodo() {
            return todo;
        }

        @Override
        public String toString() {
            return "titulos{" +
                    "foto='" + foto + '\'' +
                    ", titulo='" + titulo + '\'' +
                    ", descripcion='" + descripcion + '\'' +
                    ", categoria" + categoria +
                    '}';
        }
    }

