/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONTROLADOR;

import MODELO.Alumno;
import MODELO.AlumnoDAO;
import VISTA.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Controlador implements ActionListener {

    private Vista vista;
    private AlumnoDAO dao;
    private DefaultTableModel modelo;
    private int idAlumnoEditar = -1; // Variable para guardar el ID del alumno a editar

    public Controlador(Vista vista, AlumnoDAO dao) {
        this.vista = vista;
        this.dao = dao;
        this.modelo = (DefaultTableModel) this.vista.getTabla().getModel();
        
        // Asignar listeners a los botones
        this.vista.getBtnGuardar().addActionListener(this);
        this.vista.getBtnListar().addActionListener(this);
        this.vista.getBtnEliminar().addActionListener(this);
        this.vista.getBtnEditar().addActionListener(this);
        this.vista.getBtnOk().addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnGuardar()) {
            agregar(); // Guardar
        } else if (e.getSource() == vista.getBtnListar()) {
            listar(); // Listar
        } else if (e.getSource() == vista.getBtnEliminar()) {
            eliminar(); // Eliminar
        } else if (e.getSource() == vista.getBtnEditar()) {
            cargarDatosEnCampos(); // Cargar datos para editar
        } else if (e.getSource() == vista.getBtnOk()) {
            actualizar(); // Guardar la modificación
        }
    }

    // Método para agregar un alumno
    private void agregar() {
        Alumno alumno = new Alumno();
        alumno.setNombre(vista.getTxtNombre().getText());
        alumno.setApellido(vista.getTxtApellido().getText());
        alumno.setDni(vista.getTxtDni().getText());
        alumno.setTelefono(vista.getTxtTelefono().getText());

        dao.agregar(alumno); // Guardar alumno en la base de datos
        limpiarCampos(); // Limpiar los campos después de guardar
    }

    private void listar() {
        modelo.setRowCount(0);  // Limpiar la tabla antes de listar
        List<Alumno> lista = dao.listar(); // Obtener todos los alumnos
        int contador = 1;  // Contador para numerar los registros visiblemente
        for (Alumno alumno : lista) {
            modelo.addRow(new Object[]{
                contador,  // Mostrar número consecutivo en lugar del ID real
                alumno.getNombre(), 
                alumno.getApellido(), 
                alumno.getDni(), 
                alumno.getTelefono()
            });
            contador++; // Incrementar el contador para la siguiente fila
        }
    }



    private void eliminar() {
        int fila = vista.getTabla().getSelectedRow(); // Obtener la fila seleccionada
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un alumno para eliminar.");
        } else {
            // Cambia el índice si el ID no está en la primera columna
            int id = Integer.parseInt(vista.getTabla().getValueAt(fila, 0).toString()); // Obtener el ID del alumno seleccionado
            dao.eliminar(id); // Llamar al método DAO para eliminar el alumno
            JOptionPane.showMessageDialog(vista, "Alumno eliminado correctamente.");
            listar(); // Actualizar la tabla para reflejar los cambios
        }
    }



    private void cargarDatosEnCampos() {
    int fila = vista.getTabla().getSelectedRow();
    if (fila != -1) {
        // Ojo: El ID debe ser el de la columna correcta. 
        // Si la columna del ID no es la primera, cámbiala según tu tabla
        idAlumnoEditar = Integer.parseInt(vista.getTabla().getValueAt(fila, 0).toString()); 
        vista.getTxtNombre().setText(vista.getTabla().getValueAt(fila, 1).toString());
        vista.getTxtApellido().setText(vista.getTabla().getValueAt(fila, 2).toString());
        vista.getTxtDni().setText(vista.getTabla().getValueAt(fila, 3).toString());
        vista.getTxtTelefono().setText(vista.getTabla().getValueAt(fila, 4).toString());
    } else {
        JOptionPane.showMessageDialog(vista, "No se ha seleccionado ningún alumno.");
    }
}


  private void actualizar() {
    if (idAlumnoEditar != -1) {  // Verificar que se ha seleccionado un alumno para editar
        Alumno alumno = new Alumno();
        alumno.setId(idAlumnoEditar); // Aseguramos que el ID sea el correcto
        alumno.setNombre(vista.getTxtNombre().getText());
        alumno.setApellido(vista.getTxtApellido().getText());
        alumno.setDni(vista.getTxtDni().getText());
        alumno.setTelefono(vista.getTxtTelefono().getText());

        int resultado = dao.actualizar(alumno);  
        if (resultado == 1) {
            JOptionPane.showMessageDialog(vista, "Alumno actualizado correctamente.");
            listar();  // Refrescar la tabla inmediatamente después de actualizar
            limpiarCampos();  // Limpiar los campos después de la edición
            idAlumnoEditar = -1;  // Resetear el ID para la próxima edición
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el alumno.");
        }
    } else {
        JOptionPane.showMessageDialog(vista, "No se ha seleccionado ningún alumno para actualizar.");
    }
}




    // Método para limpiar los campos de texto en la vista
    private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtApellido().setText("");
        vista.getTxtDni().setText("");
        vista.getTxtTelefono().setText("");
    }
}
