/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.mavenprojectradio;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;


/**
 *
 * @author ihsa
 */
@ManagedBean
@ViewScoped
public class DatatableSelect implements Serializable{

    private List<PersonaDto> listaPersona;
    private PersonaDto personaSeleccionada;
    private int indexSelected;
     
    public DatatableSelect() {
    }
    
    //cargar valores    
    @PostConstruct
    public void postInit(){
        System.out.println("Cargando valores para mostrar en la grilla..");
        
        listaPersona = Arrays.asList(
                new PersonaDto(1, "Jose Alfredo Jimenez", 45),
                new PersonaDto(2, "Rodrigo X X", 50),
                new PersonaDto(3, "Miguel Y Y", 45)
        );
    }

    public void valueChangeRadioSelected(ValueChangeEvent change) {
        System.out.println("valueChangeRadioSelected");        
        
        final Object indexObject = change.getNewValue();
               
        setIndexSelected(indexObject != null ? (Integer) indexObject : -1);
        
        System.out.println("index"+indexObject);
        
        System.out.println("index se"+indexSelected);
        
        if (indexSelected != -1) {
            
            this.personaSeleccionada = listaPersona.get(indexSelected);            
            
            System.out.println("Persona seleccionada ");
            System.out.println("Id : "+this.personaSeleccionada.getId());
            System.out.println("Nombre : "+this.personaSeleccionada.getNombre());
            System.out.println("Edad : "+this.personaSeleccionada.getEdad());
        }
    }
    
    
    public List<PersonaDto> getListaPersona() {
        return listaPersona;
    }   

    public int getIndexSelected() {
        return indexSelected;
    }

    public void setIndexSelected(int indexSelected) {
        this.indexSelected = indexSelected;
    }

    public PersonaDto getPersonaSeleccionada() {
        return personaSeleccionada;
    }
    
}
