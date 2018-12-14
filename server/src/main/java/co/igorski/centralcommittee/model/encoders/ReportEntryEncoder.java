package co.igorski.centralcommittee.model.encoders;

import co.igorski.centralcommittee.model.ReportEntry;
import co.igorski.centralcommittee.ui.model.ReportEntryBean;
import com.vaadin.flow.templatemodel.ModelEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ReportEntryEncoder implements ModelEncoder<ReportEntry, ReportEntryBean> {

    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
    SimpleDateFormat fullDateFormat = new SimpleDateFormat("hh:mm:ss");

    @Override
    public ReportEntryBean encode(ReportEntry reportEntry) {

        ReportEntryBean reportEntryBean = new ReportEntryBean();
        reportEntryBean.setId(String.valueOf(reportEntry.getId()));
        reportEntryBean.setKey(reportEntry.getKey());
        reportEntryBean.setTime(timeFormat.format(reportEntry.getTimestamp()));
        reportEntryBean.setFullDate(fullDateFormat.format(reportEntry.getTimestamp()));
        reportEntryBean.setValue(reportEntry.getValue());

        return reportEntryBean;
    }

    @Override
    public ReportEntry decode(ReportEntryBean reportEntryBean) {
        ReportEntry reportEntry = new ReportEntry();

        reportEntry.setId(Long.parseLong(reportEntryBean.getId()));
        reportEntry.setKey(reportEntryBean.getKey());
        reportEntry.setValue(reportEntryBean.getValue());
        try {
            reportEntry.setTimestamp(fullDateFormat.parse(reportEntryBean.getFullDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }
}
