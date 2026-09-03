package py.edu.facitec.sistema_farmacia.util;

import java.awt.Dialog.ModalExclusionType;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ReporteUtil {

    public static void imprimir(
            List<?> lista,
            Map<String, Object> parametros,
            String nombreReporte) {

        URL url = ReporteUtil.class.getResource(
                "/jasper/" + nombreReporte + ".jasper"
        );

        if (url == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se encontró el reporte: " + nombreReporte,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (lista == null) {
            lista = Collections.emptyList();
        }

        try {
            JasperReport reporte =
                    (JasperReport) JRLoader.loadObject(url);

            JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(lista);

            JasperPrint print =
                    JasperFillManager.fillReport(
                            reporte,
                            parametros,
                            dataSource
                    );

            JasperViewer viewer =
                    new JasperViewer(print, false);
            viewer.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
            viewer.setTitle(nombreReporte);
            viewer.setVisible(true);

        } catch (JRException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error al generar el reporte:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
