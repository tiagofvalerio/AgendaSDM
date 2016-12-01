package br.edu.ifspsaocarlos.sdm.agendasdm;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.agendasdm.model.Contato;

public class AgendaActivity extends ListActivity {

    public static final String PARAMETRO_CONTATO = "PARAMETRO_CONTATO";
    private final int ADICIONA_EDITA_CONTATO = 0;
    private List<Contato> listaContatos; // Lista de contatos estática para testes
    private ContatoArrayAdapter adaptador;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Intent intent = getIntent();
        listaContatos = new ArrayList<Contato>();
        listaContatos.add(new Contato(1, "Pedro", "(16)3351-9607", "nobile@ifsp.edu.br"));
        listaContatos.add(new Contato(2, "Pablo", "(16)3351-9608", "pablo@ifsp.edu.br"));
        listaContatos.add(new Contato(3, "Silvana", "(16)3351-9609", "silvana@ifsp.edu.br"));
        listaContatos.add(new Contato(4, "Eloize", "(16)3351-9610", "eloize@ifsp.edu.br"));
        listaContatos.add(new Contato(5, "Danilo", "(16)3351-9611", "danilo@ifsp.edu.br"));
        listaContatos.add(new Contato(6, "Rodrigo", "(16)3351-9612", "rodrigo@ifsp.edu.br"));
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String nome = intent.getStringExtra(SearchManager.QUERY);
            List<Contato> contatosEncontrados = null;
            for (Contato contato : listaContatos) {
                if (contato.getNome().contains(nome)) {
                    if (contatosEncontrados == null) {
                        contatosEncontrados = new ArrayList<Contato>();
                    }
                    contatosEncontrados.add(contato);
                }
            }
            adaptador = new ContatoArrayAdapter(this, contatosEncontrados);
        } else {
            /* Preenchendo lista de contatos */
            adaptador = new ContatoArrayAdapter(this, listaContatos);
        }
        setListAdapter(adaptador);
        registerForContextMenu(getListView());
    }

    public void onListItemClick(ListView l, View v, int posicao, long id) {
        ContatoArrayAdapter adaptador = (ContatoArrayAdapter) getListAdapter();
        Contato contato = adaptador.getItem(posicao);
        Intent i = new Intent(this, DetalheActivity.class);
        i.putExtra(PARAMETRO_CONTATO, contato);
        startActivityForResult(i, ADICIONA_EDITA_CONTATO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADICIONA_EDITA_CONTATO) {
            if (resultCode == RESULT_OK) {
                Contato contatoRetornado = (Contato) data.getSerializableExtra(PARAMETRO_CONTATO);
                if (contatoRetornado.getId() == Contato.ID_INVALIDO) {
                    // Novo contato
                    contatoRetornado.setId(getListAdapter().getCount() + 1);
                    listaContatos.add(contatoRetornado);
                } else {
                    // Atualiza contato existente
                    atualizaContato(contatoRetornado);
                }
                ((ArrayAdapter<Contato>) getListAdapter()).notifyDataSetChanged();
            }
        }
    }

    private void atualizaContato(Contato contatoAtualizado) {
        for (int posicao = 0; posicao < getListAdapter().getCount(); posicao++) {
            Contato contatoAntigo = (Contato) getListAdapter().getItem(posicao);
            if (contatoAtualizado.getId() == contatoAntigo.getId()) {
                contatoAntigo.setNome(contatoAtualizado.getNome());
                contatoAntigo.setEmail(contatoAtualizado.getEmail());
                contatoAntigo.setFone(contatoAtualizado.getFone());
                contatoAntigo.setFoneAdicional(contatoAtualizado.getFoneAdicional());
                break;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.adicionar_contato) {
            Intent adicionarContatoIntet = new Intent(this, DetalheActivity.class);
            startActivityForResult(adicionarContatoIntet, ADICIONA_EDITA_CONTATO);
            return true;
        } else {
            if (id == R.id.pesquisar_contato) {
                onSearchRequested();
                return true;
            }
        }
        return false;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_flutuante, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo informacoes = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        ContatoArrayAdapter adaptador = (ContatoArrayAdapter) getListAdapter();
        Contato contato = adaptador.getItem(informacoes.position);
        switch (item.getItemId()) {
            case R.id.remover_contato:
                listaContatos.remove(contato);
                Toast.makeText(this, getString(R.string.mensagem_removido), Toast.LENGTH_SHORT).show();
                ((ArrayAdapter<Contato>) getListAdapter()).notifyDataSetChanged();
                break;
            case R.id.chamar_contato:
                Intent chamarContatoIntent = new Intent(Intent.ACTION_CALL);
                String numeroTelefone = "tel: " + contato.getFone();
                chamarContatoIntent.setData(Uri.parse(numeroTelefone));
                try {
                    startActivity(chamarContatoIntent);
                } catch (SecurityException se) {
                    Toast.makeText(this, getString(R.string.mensagem_erro_chamada),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
}