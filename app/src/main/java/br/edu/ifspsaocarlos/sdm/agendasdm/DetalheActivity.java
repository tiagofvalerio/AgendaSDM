package br.edu.ifspsaocarlos.sdm.agendasdm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.agendasdm.model.Contato;

public class DetalheActivity extends Activity {

    private Contato contato;
    private EditText etNome;
    private EditText etFone;
    private EditText etFoneAdicinal;
    private EditText etEmail;
    private TextView tvFoneAdicional;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        etNome = (EditText) findViewById(R.id.et_nome);
        etFone = (EditText) findViewById(R.id.et_fone);
        etFoneAdicinal = (EditText) findViewById(R.id.et_fone_adicional);
        etEmail = (EditText) findViewById(R.id.et_email);
        tvFoneAdicional = (TextView)findViewById(R.id.tv_fone_adicional);
        if (getIntent().hasExtra(AgendaActivity.PARAMETRO_CONTATO)) {
            contato = (Contato) getIntent().getSerializableExtra(AgendaActivity.PARAMETRO_CONTATO);
            etNome.setText(contato.getNome());
            etFone.setText(contato.getFone());
            etFoneAdicinal.setText(contato.getFoneAdicional());
            etEmail.setText(contato.getEmail());
        }

        String foneAdicional = etFoneAdicinal.getText().toString();
        if(foneAdicional != null && !foneAdicional.isEmpty()){
            tvFoneAdicional.setVisibility(View.VISIBLE);
            etFoneAdicinal.setVisibility(View.VISIBLE);
        } else {
            tvFoneAdicional.setVisibility(View.INVISIBLE);
            etFoneAdicinal.setVisibility(View.INVISIBLE);
        }
    /* Rotinas de abertura de conexão para persistência */
    }

    public void onClick(View v) {
        String nome = etNome.getText().toString();
        String fone = etFone.getText().toString();
        String foneAdicional = etFoneAdicinal.getText().toString();
        String email = etEmail.getText().toString();
        if (contato == null) {
            contato = new Contato();
            contato.setId(Contato.ID_INVALIDO);
            contato.setNome(nome);
            contato.setFone(fone);
            contato.setFoneAdicional(foneAdicional);
            contato.setEmail(email);
            /* Rotinas de salvamento de novo contato */
            Toast.makeText(this, getString(R.string.mensagem_incluido), Toast.LENGTH_SHORT).show();
        } else {
            contato.setNome(nome);
            contato.setFone(fone);
            contato.setFoneAdicional(foneAdicional);
            contato.setEmail(email);
            /* Rotina de atualização de contato existente */
            Toast.makeText(this, getString(R.string.mensagem_alterado), Toast.LENGTH_SHORT).show();
        }
        /* Retornando para a tela anterior */
        Intent intentResultado = new Intent();
        intentResultado.putExtra(AgendaActivity.PARAMETRO_CONTATO, contato);
        setResult(RESULT_OK, intentResultado);
        finish();
    }

    public void escondeCamposFoneAdicional(View v) {
        TextView tv = (TextView)findViewById(R.id.tv_fone_adicional);
        EditText et = (EditText)findViewById(R.id.et_fone_adicional);
        tv.setVisibility(View.INVISIBLE);
        et.setVisibility(View.INVISIBLE);
    }

    public void mostraCamposFoneAdicional(View v) {
        TextView tv = (TextView)findViewById(R.id.tv_fone_adicional);
        EditText et = (EditText)findViewById(R.id.et_fone_adicional);
        tv.setVisibility(View.VISIBLE);
        et.setVisibility(View.VISIBLE);
    }
}
