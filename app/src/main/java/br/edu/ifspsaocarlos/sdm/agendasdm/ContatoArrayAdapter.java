package br.edu.ifspsaocarlos.sdm.agendasdm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.agendasdm.model.Contato;

public class ContatoArrayAdapter extends ArrayAdapter<Contato> {

    private LayoutInflater inflater;

    public ContatoArrayAdapter(Activity tela, List<Contato> contatos) {
        super(tela, R.layout.row, contatos);
        inflater = tela.getLayoutInflater();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null);
        }
        Contato contato = getItem(position);
        ((TextView) convertView.findViewById(R.id.tv_nome)).setText(contato.getNome());
        ((TextView) convertView.findViewById(R.id.tv_emailtelefone)).setText(contato.getFone() + " / " + contato.getEmail());
        return convertView;
    }
}
