package view;

import model.*;
import Services.SistemaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private SistemaService servico;
    private JTabbedPane abas;

    private DefaultTableModel modeloClubes;
    private DefaultTableModel modeloCampeonatos;
    private DefaultTableModel modeloPartidas;
    private DefaultTableModel modeloParticipantes;
    private DefaultTableModel modeloGrupos;
    private DefaultTableModel modeloClassificacao;
    private DefaultTableModel modeloApostas;

    public TelaPrincipal() {
        servico = new SistemaService();
        configurarJanela();
        criarAbas();
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Sistema de Apostas - Campeonato de Futebol");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void criarAbas() {
        abas = new JTabbedPane();
        abas.addTab("Clubes", criarPainelClubes());
        abas.addTab("Campeonatos", criarPainelCampeonatos());
        abas.addTab("Partidas", criarPainelPartidas());
        abas.addTab("Participantes", criarPainelParticipantes());
        abas.addTab("Grupos", criarPainelGrupos());
        abas.addTab("Apostas", criarPainelApostas());
        abas.addTab("Resultados", criarPainelResultados());
        abas.addTab("Classificação", criarPainelClassificacao());
        add(abas);
    }

    private JPanel criarPainelClubes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField txtNome = new JTextField();
        JTextField txtSigla = new JTextField();
        form.add(new JLabel("Nome do Clube:"));
        form.add(txtNome);
        form.add(new JLabel("Sigla:"));
        form.add(txtSigla);

        JButton btnCadastrar = new JButton("Cadastrar Clube");
        modeloClubes = new DefaultTableModel(new String[]{"ID", "Nome", "Sigla"}, 0);
        JTable tabela = new JTable(modeloClubes);

        btnCadastrar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String sigla = txtSigla.getText().trim();
            if (nome.isEmpty() || sigla.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
            Clube clube = servico.cadastrarClube(nome, sigla);
            modeloClubes.addRow(new Object[]{clube.getId(), clube.getNome(), clube.getSigla()});
            txtNome.setText("");
            txtSigla.setText("");
        });

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(form, BorderLayout.CENTER);
        topo.add(btnCadastrar, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelCampeonatos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(1, 2, 5, 5));
        JTextField txtNome = new JTextField();
        form.add(new JLabel("Nome do Campeonato:"));
        form.add(txtNome);

        JButton btnCriar = new JButton("Criar Campeonato");
        modeloCampeonatos = new DefaultTableModel(new String[]{"ID", "Nome", "Clubes"}, 0);
        JTable tabela = new JTable(modeloCampeonatos);

        btnCriar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome.");
                return;
            }
            servico.cadastrarCampeonato(nome);
            atualizarTabelaCampeonatos();
            txtNome.setText("");
        });

        JPanel painelAdicionar = new JPanel(new GridLayout(1, 3, 5, 5));
        JComboBox<Campeonato> comboCamp = new JComboBox<>();
        JComboBox<Clube> comboClube = new JComboBox<>();
        JButton btnAdicionar = new JButton("Adicionar Clube ao Campeonato");

        painelAdicionar.add(comboCamp);
        painelAdicionar.add(comboClube);
        painelAdicionar.add(btnAdicionar);

        btnAdicionar.addActionListener(e -> {
            Campeonato camp = (Campeonato) comboCamp.getSelectedItem();
            Clube clube = (Clube) comboClube.getSelectedItem();
            if (camp == null || clube == null) {
                JOptionPane.showMessageDialog(this, "Selecione campeonato e clube.");
                return;
            }
            if (!servico.adicionarClubeAoCampeonato(camp, clube)) {
                JOptionPane.showMessageDialog(this, "Não foi possível adicionar. Limite de 8 clubes ou clube já adicionado.");
                return;
            }
            atualizarTabelaCampeonatos();
        });

        abas.addChangeListener(e -> {
            comboCamp.removeAllItems();
            for (Campeonato c : servico.getCampeonatos()) {
                comboCamp.addItem(c);
            }
            comboClube.removeAllItems();
            for (Clube c : servico.getClubes()) {
                comboClube.addItem(c);
            }
        });

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        JPanel topoForm = new JPanel(new BorderLayout(5, 5));
        topoForm.add(form, BorderLayout.CENTER);
        topoForm.add(btnCriar, BorderLayout.SOUTH);
        topo.add(topoForm, BorderLayout.NORTH);
        topo.add(painelAdicionar, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private void atualizarTabelaCampeonatos() {
        modeloCampeonatos.setRowCount(0);
        for (Campeonato c : servico.getCampeonatos()) {
            StringBuilder clubes = new StringBuilder();
            for (int i = 0; i < c.getClubes().size(); i++) {
                if (i > 0) clubes.append(", ");
                clubes.append(c.getClubes().get(i).getSigla());
            }
            modeloCampeonatos.addRow(new Object[]{c.getId(), c.getNome(), clubes.toString()});
        }
    }

    private JPanel criarPainelPartidas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<Campeonato> comboCamp = new JComboBox<>();
        JComboBox<Clube> comboCasa = new JComboBox<>();
        JComboBox<Clube> comboVisitante = new JComboBox<>();
        JTextField txtDataHora = new JTextField("dd/MM/yyyy HH:mm");

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Campeonato:"));
        form.add(comboCamp);
        form.add(new JLabel("Clube Casa:"));
        form.add(comboCasa);
        form.add(new JLabel("Clube Visitante:"));
        form.add(comboVisitante);
        form.add(new JLabel("Data/Hora (dd/MM/yyyy HH:mm):"));
        form.add(txtDataHora);

        JButton btnCadastrar = new JButton("Cadastrar Partida");
        modeloPartidas = new DefaultTableModel(new String[]{"ID", "Campeonato", "Casa", "Visitante", "Data/Hora", "Status"}, 0);
        JTable tabela = new JTable(modeloPartidas);

        comboCamp.addActionListener(e -> {
            comboCasa.removeAllItems();
            comboVisitante.removeAllItems();
            Campeonato camp = (Campeonato) comboCamp.getSelectedItem();
            if (camp != null) {
                for (Clube c : camp.getClubes()) {
                    comboCasa.addItem(c);
                    comboVisitante.addItem(c);
                }
            }
        });

        btnCadastrar.addActionListener(e -> {
            Campeonato camp = (Campeonato) comboCamp.getSelectedItem();
            Clube casa = (Clube) comboCasa.getSelectedItem();
            Clube visitante = (Clube) comboVisitante.getSelectedItem();
            if (camp == null || casa == null || visitante == null) {
                JOptionPane.showMessageDialog(this, "Selecione todos os campos.");
                return;
            }
            if (casa.getId() == visitante.getId()) {
                JOptionPane.showMessageDialog(this, "Selecione clubes diferentes.");
                return;
            }
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime dataHora = LocalDateTime.parse(txtDataHora.getText().trim(), fmt);
                servico.cadastrarPartida(camp, casa, visitante, dataHora);
                atualizarTabelaPartidas();
                txtDataHora.setText("");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data/hora inválida. Use o formato dd/MM/yyyy HH:mm");
            }
        });

        abas.addChangeListener(e -> {
            comboCamp.removeAllItems();
            for (Campeonato c : servico.getCampeonatos()) {
                comboCamp.addItem(c);
            }
            atualizarTabelaPartidas();
        });

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(form, BorderLayout.CENTER);
        topo.add(btnCadastrar, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private void atualizarTabelaPartidas() {
        modeloPartidas.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Campeonato camp : servico.getCampeonatos()) {
            for (Partida p : camp.getPartidas()) {
                String status = p.isFinalizada() ? p.getGolsCasa() + " x " + p.getGolsVisitante() : "Pendente";
                modeloPartidas.addRow(new Object[]{
                        p.getId(), camp.getNome(),
                        p.getClubeCasa().getNome(), p.getClubeVisitante().getNome(),
                        p.getDataHora().format(fmt), status
                });
            }
        }
    }

    private JPanel criarPainelParticipantes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Email:"));
        form.add(txtEmail);

        JButton btnCadastrar = new JButton("Cadastrar Participante");
        modeloParticipantes = new DefaultTableModel(new String[]{"ID", "Nome", "Email"}, 0);
        JTable tabela = new JTable(modeloParticipantes);

        btnCadastrar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            if (nome.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
            Participante p = servico.cadastrarParticipante(nome, email);
            modeloParticipantes.addRow(new Object[]{p.getId(), p.getNome(), p.getEmail()});
            txtNome.setText("");
            txtEmail.setText("");
        });

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(form, BorderLayout.CENTER);
        topo.add(btnCadastrar, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelGrupos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCriar = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField txtNomeGrupo = new JTextField();
        JComboBox<Campeonato> comboCamp = new JComboBox<>();
        formCriar.add(new JLabel("Nome do Grupo:"));
        formCriar.add(txtNomeGrupo);
        formCriar.add(new JLabel("Campeonato:"));
        formCriar.add(comboCamp);

        JButton btnCriar = new JButton("Criar Grupo");

        JPanel formAdd = new JPanel(new GridLayout(1, 3, 5, 5));
        JComboBox<GrupoApostas> comboGrupo = new JComboBox<>();
        JComboBox<Participante> comboParticipante = new JComboBox<>();
        JButton btnAdicionar = new JButton("Adicionar ao Grupo");
        formAdd.add(comboGrupo);
        formAdd.add(comboParticipante);
        formAdd.add(btnAdicionar);

        modeloGrupos = new DefaultTableModel(new String[]{"ID", "Grupo", "Campeonato", "Participantes"}, 0);
        JTable tabela = new JTable(modeloGrupos);

        btnCriar.addActionListener(e -> {
            String nome = txtNomeGrupo.getText().trim();
            Campeonato camp = (Campeonato) comboCamp.getSelectedItem();
            if (nome.isEmpty() || camp == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }
            GrupoApostas grupo = servico.criarGrupo(nome, camp);
            if (grupo == null) {
                JOptionPane.showMessageDialog(this, "Limite de 5 grupos atingido.");
                return;
            }
            atualizarTabelaGrupos();
            txtNomeGrupo.setText("");
        });

        btnAdicionar.addActionListener(e -> {
            GrupoApostas grupo = (GrupoApostas) comboGrupo.getSelectedItem();
            Participante part = (Participante) comboParticipante.getSelectedItem();
            if (grupo == null || part == null) {
                JOptionPane.showMessageDialog(this, "Selecione grupo e participante.");
                return;
            }
            if (!servico.adicionarParticipanteAoGrupo(grupo, part)) {
                JOptionPane.showMessageDialog(this, "Não foi possível adicionar. Limite de 5 ou já cadastrado.");
                return;
            }
            atualizarTabelaGrupos();
        });

        abas.addChangeListener(e -> {
            comboCamp.removeAllItems();
            for (Campeonato c : servico.getCampeonatos()) {
                comboCamp.addItem(c);
            }
            comboGrupo.removeAllItems();
            for (GrupoApostas g : servico.getGrupos()) {
                comboGrupo.addItem(g);
            }
            comboParticipante.removeAllItems();
            for (Participante p : servico.getParticipantes()) {
                comboParticipante.addItem(p);
            }
            atualizarTabelaGrupos();
        });

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        JPanel topoForm = new JPanel(new BorderLayout(5, 5));
        topoForm.add(formCriar, BorderLayout.CENTER);
        topoForm.add(btnCriar, BorderLayout.SOUTH);
        topo.add(topoForm, BorderLayout.NORTH);
        topo.add(formAdd, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private void atualizarTabelaGrupos() {
        modeloGrupos.setRowCount(0);
        for (GrupoApostas g : servico.getGrupos()) {
            StringBuilder parts = new StringBuilder();
            for (int i = 0; i < g.getParticipantes().size(); i++) {
                if (i > 0) parts.append(", ");
                parts.append(g.getParticipantes().get(i).getNome());
            }
            modeloGrupos.addRow(new Object[]{g.getId(), g.getNome(), g.getCampeonato().getNome(), parts.toString()});
        }
    }

    private JPanel criarPainelApostas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<Participante> comboPart = new JComboBox<>();
        JComboBox<Partida> comboPartida = new JComboBox<>();
        JComboBox<Campeonato> comboCamp = new JComboBox<>();
        JTextField txtGolsCasa = new JTextField();
        JTextField txtGolsVisitante = new JTextField();

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Participante:"));
        form.add(comboPart);
        form.add(new JLabel("Campeonato:"));
        form.add(comboCamp);
        form.add(new JLabel("Partida:"));
        form.add(comboPartida);
        form.add(new JLabel("Gols Casa (aposta):"));
        form.add(txtGolsCasa);
        form.add(new JLabel("Gols Visitante (aposta):"));
        form.add(txtGolsVisitante);

        JButton btnApostar = new JButton("Registrar Aposta");
        modeloApostas = new DefaultTableModel(new String[]{"Participante", "Partida", "Aposta", "Pontos"}, 0);
        JTable tabela = new JTable(modeloApostas);

        comboCamp.addActionListener(e -> {
            comboPartida.removeAllItems();
            Campeonato camp = (Campeonato) comboCamp.getSelectedItem();
            if (camp != null) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (Partida p : camp.getPartidas()) {
                    comboPartida.addItem(p);
                }
            }
        });

        btnApostar.addActionListener(e -> {
            Participante part = (Participante) comboPart.getSelectedItem();
            Partida partida = (Partida) comboPartida.getSelectedItem();
            if (part == null || partida == null) {
                JOptionPane.showMessageDialog(this, "Selecione participante e partida.");
                return;
            }
            try {
                int golsCasa = Integer.parseInt(txtGolsCasa.getText().trim());
                int golsVisitante = Integer.parseInt(txtGolsVisitante.getText().trim());
                if (golsCasa < 0 || golsVisitante < 0) {
                    JOptionPane.showMessageDialog(this, "Gols não podem ser negativos.");
                    return;
                }
                Aposta aposta = servico.registrarAposta(part, partida, golsCasa, golsVisitante);
                if (aposta == null) {
                    JOptionPane.showMessageDialog(this, "Não foi possível registrar. Prazo expirado ou aposta já existe.");
                    return;
                }
                atualizarTabelaApostas();
                txtGolsCasa.setText("");
                txtGolsVisitante.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Informe números válidos para os gols.");
            }
        });

        abas.addChangeListener(e -> {
            comboPart.removeAllItems();
            for (Participante p : servico.getParticipantes()) {
                comboPart.addItem(p);
            }
            comboCamp.removeAllItems();
            for (Campeonato c : servico.getCampeonatos()) {
                comboCamp.addItem(c);
            }
            atualizarTabelaApostas();
        });

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(form, BorderLayout.CENTER);
        topo.add(btnApostar, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private void atualizarTabelaApostas() {
        modeloApostas.setRowCount(0);
        for (Aposta a : servico.getApostas()) {
            modeloApostas.addRow(new Object[]{
                    a.getParticipante().getNome(),
                    a.getPartida().exibirResumo(),
                    a.getGolsCasaAposta() + " x " + a.getGolsVisitanteAposta(),
                    a.calcularPontos()
            });
        }
    }

    private JPanel criarPainelResultados() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<Campeonato> comboCamp = new JComboBox<>();
        JComboBox<Partida> comboPartida = new JComboBox<>();
        JTextField txtGolsCasa = new JTextField();
        JTextField txtGolsVisitante = new JTextField();

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Campeonato:"));
        form.add(comboCamp);
        form.add(new JLabel("Partida:"));
        form.add(comboPartida);
        form.add(new JLabel("Gols Casa (real):"));
        form.add(txtGolsCasa);
        form.add(new JLabel("Gols Visitante (real):"));
        form.add(txtGolsVisitante);

        JButton btnRegistrar = new JButton("Registrar Resultado");

        comboCamp.addActionListener(e -> {
            comboPartida.removeAllItems();
            Campeonato camp = (Campeonato) comboCamp.getSelectedItem();
            if (camp != null) {
                for (Partida p : camp.getPartidas()) {
                    if (!p.isFinalizada()) {
                        comboPartida.addItem(p);
                    }
                }
            }
        });

        btnRegistrar.addActionListener(e -> {
            Partida partida = (Partida) comboPartida.getSelectedItem();
            if (partida == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma partida.");
                return;
            }
            try {
                int golsCasa = Integer.parseInt(txtGolsCasa.getText().trim());
                int golsVisitante = Integer.parseInt(txtGolsVisitante.getText().trim());
                if (golsCasa < 0 || golsVisitante < 0) {
                    JOptionPane.showMessageDialog(this, "Gols não podem ser negativos.");
                    return;
                }
                servico.registrarResultado(partida, golsCasa, golsVisitante);
                JOptionPane.showMessageDialog(this, "Resultado registrado com sucesso!");
                txtGolsCasa.setText("");
                txtGolsVisitante.setText("");
                comboPartida.removeItem(partida);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Informe números válidos.");
            }
        });

        abas.addChangeListener(e -> {
            comboCamp.removeAllItems();
            for (Campeonato c : servico.getCampeonatos()) {
                comboCamp.addItem(c);
            }
        });

        JPanel topo = new JPanel(new BorderLayout(5, 5));
        topo.add(form, BorderLayout.CENTER);
        topo.add(btnRegistrar, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        return painel;
    }

    private JPanel criarPainelClassificacao() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<GrupoApostas> comboGrupo = new JComboBox<>();
        JButton btnAtualizar = new JButton("Ver Classificação");

        JPanel form = new JPanel(new GridLayout(1, 3, 5, 5));
        form.add(new JLabel("Grupo:"));
        form.add(comboGrupo);
        form.add(btnAtualizar);

        modeloClassificacao = new DefaultTableModel(new String[]{"Posição", "Participante", "Pontos"}, 0);
        JTable tabela = new JTable(modeloClassificacao);

        btnAtualizar.addActionListener(e -> {
            GrupoApostas grupo = (GrupoApostas) comboGrupo.getSelectedItem();
            if (grupo == null) {
                JOptionPane.showMessageDialog(this, "Selecione um grupo.");
                return;
            }
            modeloClassificacao.setRowCount(0);
            List<Participante> classificacao = grupo.getClassificacao();
            for (int i = 0; i < classificacao.size(); i++) {
                Participante p = classificacao.get(i);
                modeloClassificacao.addRow(new Object[]{(i + 1) + "º", p.getNome(), p.calcularPontos()});
            }
        });

        abas.addChangeListener(e -> {
            comboGrupo.removeAllItems();
            for (GrupoApostas g : servico.getGrupos()) {
                comboGrupo.addItem(g);
            }
        });

        painel.add(form, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaPrincipal::new);
    }
}

