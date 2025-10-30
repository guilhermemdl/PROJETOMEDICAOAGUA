package org.example;

import org.example.dao.*;
import org.example.model.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class MainConsole {

    private static final PessoaFisicaDAO pfDAO = new PessoaFisicaDAO();
    private static final PessoaJuridicaDAO pjDAO = new PessoaJuridicaDAO();
    private static final ImovelDAO imovelDAO = new ImovelDAO();
    private static final ContratoDAO contratoDAO = new ContratoDAO();
    private static final HidrometroDAO hidrometroDAO = new HidrometroDAO();

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- SISTEMA DE GERENCIAMENTO DE HIDRÔMETROS ---");
        loopPrincipal();
    }

    private static void loopPrincipal() {
        while (true) {
            System.out.println("\n== MENU PRINCIPAL ==");
            System.out.println("1. Área do Técnico");
            System.out.println("2. Área do Cliente");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();
            
            switch (opcao) {
                case 1:
                    menuTecnico();
                    break;
                case 2:
                    menuCliente();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    scanner.close();
                    System.exit(0);
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuTecnico() {
        while (true) {
            System.out.println("\n== MENU DO TÉCNICO ==");
            System.out.println("1. Gerenciar Pessoas Físicas");
            System.out.println("2. Gerenciar Pessoas Jurídicas");
            System.out.println("3. Gerenciar Imóveis");
            System.out.println("4. Gerenciar Contratos");
            System.out.println("5. Gerenciar Hidrômetros");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();
            
            switch (opcao) {
                case 1: gerenciarPessoasFisicas(); break;
                case 2: gerenciarPessoasJuridicas(); break;
                case 3: gerenciarImoveis(); break;
                case 4: gerenciarContratos(); break;
                case 5: gerenciarHidrometros(); break;
                case 0: return;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static void gerenciarPessoasFisicas() {
        while (true) {
            System.out.println("\n-- Gerenciar Pessoas Físicas --");
            System.out.println("1. Adicionar Pessoa Física");
            System.out.println("2. Listar Pessoas Físicas");
            System.out.println("3. Editar Pessoa Física");
            System.out.println("4. Excluir Pessoa Física");
            System.out.println("0. Voltar ao Menu Técnico");
            System.out.print("Escolha uma opção: ");
            
            int opcao = lerInteiro();
            
            try {
                switch (opcao) {
                    case 1: adicionarPessoaFisica(); break;
                    case 2: listarPessoasFisicas(); break;
                    case 3: editarPessoaFisica(); break;
                    case 4: excluirPessoaFisica(); break;
                    case 0: return;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void adicionarPessoaFisica() throws SQLException {
        System.out.println("\n[Adicionar Pessoa Física]");
        System.out.print("Nome: ");
        String nome = lerString();
        System.out.print("Email: ");
        String email = lerString();
        System.out.print("CPF (só números): ");
        String cpf = lerString();
        System.out.print("Data Nasc. (AAAA-MM-DD): ");
        LocalDate dataNasc = lerData();

        PessoaFisica pf = new PessoaFisica(nome, email, cpf, dataNasc);
        pfDAO.inserirPessoaFisica(pf);
        System.out.println("Pessoa Física (ID: " + pf.getCodUsuario() + ") adicionada com sucesso!");
    }

    private static void listarPessoasFisicas() throws SQLException {
        System.out.println("\n[Lista de Pessoas Físicas]");
        List<PessoaFisica> lista = pfDAO.listarPessoasFisicas();
        if (lista.isEmpty()) {
            System.out.println("Nenhum cliente (PF) cadastrado.");
            return;
        }
        for (PessoaFisica pf : lista) {
            System.out.printf("ID: %d | Nome: %s | CPF: %s | Email: %s | Nasc: %s\n",
                pf.getCodUsuario(), pf.getNome(), pf.getCpf(), pf.getEmail(), pf.getDataNascimento());
        }
    }

    private static void editarPessoaFisica() throws SQLException {
        System.out.print("\nDigite o ID da Pessoa Física para editar: ");
        int id = lerInteiro();
        PessoaFisica pf = pfDAO.buscarPessoaFisicaPorId(id);
        if (pf == null) {
            System.out.println("Pessoa Física não encontrada.");
            return;
        }

        System.out.print("Novo Nome (Atual: " + pf.getNome() + "): ");
        pf.setNome(lerString());
        System.out.print("Novo Email (Atual: " + pf.getEmail() + "): ");
        pf.setEmail(lerString());
        System.out.print("Novo CPF (Atual: " + pf.getCpf() + "): ");
        pf.setCpf(lerString());
        System.out.print("Nova Data Nasc. (AAAA-MM-DD) (Atual: " + pf.getDataNascimento() + "): ");
        pf.setDataNascimento(lerData());

        pfDAO.atualizarPessoaFisica(pf);
        System.out.println("Pessoa Física atualizada com sucesso!");
    }

    private static void excluirPessoaFisica() throws SQLException {
        System.out.print("\nDigite o ID da Pessoa Física para excluir: ");
        int id = lerInteiro();
        
        PessoaFisica pf = pfDAO.buscarPessoaFisicaPorId(id);
        if (pf == null) {
            System.out.println("Pessoa Física não encontrada.");
            return;
        }
        
        System.out.print("Tem certeza que deseja excluir " + pf.getNome() + " (S/N)? ");
        if (lerString().equalsIgnoreCase("S")) {
            pfDAO.excluirPessoaFisica(id);
            System.out.println("Pessoa Física excluída com sucesso.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static void gerenciarPessoasJuridicas() {
        while (true) {
            System.out.println("\n-- Gerenciar Pessoas Jurídicas --");
            System.out.println("1. Adicionar Pessoa Jurídica");
            System.out.println("2. Listar Pessoas Jurídicas");
            System.out.println("3. Editar Pessoa Jurídica");
            System.out.println("4. Excluir Pessoa Jurídica");
            System.out.println("0. Voltar ao Menu Técnico");
            System.out.print("Escolha uma opção: ");
            
            int opcao = lerInteiro();
            try {
                switch(opcao) {
                    case 1: adicionarPessoaJuridica(); break;
                    case 2: listarPessoasJuridicas(); break;
                    case 3: editarPessoaJuridica(); break;
                    case 4: excluirPessoaJuridica(); break;
                    case 0: return;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void adicionarPessoaJuridica() throws SQLException {
        System.out.println("\n[Adicionar Pessoa Jurídica]");
        System.out.print("Nome Fantasia: ");
        String nome = lerString();
        System.out.print("Email: ");
        String email = lerString();
        System.out.print("CNPJ (só números): ");
        String cnpj = lerString();
        System.out.print("Razão Social: ");
        String razao = lerString();

        PessoaJuridica pj = new PessoaJuridica(nome, email, cnpj, razao);
        pjDAO.inserirPessoaJuridica(pj);
        System.out.println("Pessoa Jurídica (ID: " + pj.getCodUsuario() + ") adicionada com sucesso!");
    }

    private static void listarPessoasJuridicas() throws SQLException {
        System.out.println("\n[Lista de Pessoas Jurídicas]");
        List<PessoaJuridica> lista = pjDAO.listarPessoasJuridicas();
        if (lista.isEmpty()) {
            System.out.println("Nenhum cliente (PJ) cadastrado.");
            return;
        }
        for (PessoaJuridica pj : lista) {
            System.out.printf("ID: %d | Nome: %s | CNPJ: %s | Razão Social: %s\n",
                pj.getCodUsuario(), pj.getNome(), pj.getCnpj(), pj.getRazaoSocial());
        }
    }

    private static void editarPessoaJuridica() throws SQLException {
        System.out.print("\nDigite o ID da Pessoa Jurídica para editar: ");
        int id = lerInteiro();
        PessoaJuridica pj = pjDAO.buscarPessoaJuridicaPorId(id);
        if (pj == null) {
            System.out.println("Pessoa Jurídica não encontrada.");
            return;
        }

        System.out.print("Novo Nome Fantasia (Atual: " + pj.getNome() + "): ");
        pj.setNome(lerString());
        System.out.print("Novo Email (Atual: " + pj.getEmail() + "): ");
        pj.setEmail(lerString());
        System.out.print("Novo CNPJ (Atual: " + pj.getCnpj() + "): ");
        pj.setCnpj(lerString());
        System.out.print("Nova Razão Social (Atual: " + pj.getRazaoSocial() + "): ");
        pj.setRazaoSocial(lerString());

        pjDAO.atualizarPessoaJuridica(pj);
        System.out.println("Pessoa Jurídica atualizada com sucesso!");
    }

    private static void excluirPessoaJuridica() throws SQLException {
        System.out.print("\nDigite o ID da Pessoa Jurídica para excluir: ");
        int id = lerInteiro();
        
        PessoaJuridica pj = pjDAO.buscarPessoaJuridicaPorId(id);
        if (pj == null) {
            System.out.println("Pessoa Jurídica não encontrada.");
            return;
        }
        
        System.out.print("Tem certeza que deseja excluir " + pj.getNome() + " (S/N)? ");
        if (lerString().equalsIgnoreCase("S")) {
            pjDAO.excluirPessoaJuridica(id);
            System.out.println("Pessoa Jurídica excluída com sucesso.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static void gerenciarImoveis() {
        while (true) {
            System.out.println("\n-- Gerenciar Imóveis --");
            System.out.println("1. Adicionar Imóvel");
            System.out.println("2. Listar Imóveis");
            System.out.println("3. Editar Imóvel");
            System.out.println("4. Excluir Imóvel");
            System.out.println("0. Voltar ao Menu Técnico");
            
            int opcao = lerInteiro();
            try {
                switch(opcao) {
                    case 1: adicionarImovel(); break;
                    case 2: listarImoveis(); break;
                    case 3: editarImovel(); break;
                    case 4: excluirImovel(); break;
                    case 0: return;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }
    
    private static void adicionarImovel() throws SQLException {
        System.out.println("\n[Adicionar Imóvel - 1º Endereço]");
        System.out.print("CEP (só números): ");
        String cep = lerString();
        System.out.print("Rua: ");
        String rua = lerString();
        System.out.print("Bairro: ");
        String bairro = lerString();
        System.out.print("Cidade: ");
        String cidade = lerString();
        System.out.print("Estado (UF): ");
        String estado = lerString();
        Endereco end = new Endereco(cep, rua, bairro, cidade, estado);
        
        System.out.println("[2º Imóvel]");
        System.out.print("Tipo do Imóvel (Residencial/Comercial): ");
        String tipo = lerString();
        
        Imovel imovel = new Imovel(0, tipo, end);
        imovelDAO.inserirImovel(imovel);
        System.out.println("Imóvel (ID: " + imovel.getCod_imovel() + ") adicionado com sucesso!");
    }

    private static void listarImoveis() throws SQLException {
        System.out.println("\n[Lista de Imóveis]");
        List<Imovel> lista = imovelDAO.listarImoveis();
        if (lista.isEmpty()) {
            System.out.println("Nenhum imóvel cadastrado.");
            return;
        }
        for (Imovel i : lista) {
            System.out.printf("ID: %d | Tipo: %s | Endereço: %s, %s - %s\n",
                i.getCod_imovel(), i.getTipo_imovel(), i.getEndereco().getRua(), i.getEndereco().getBairro(), i.getEndereco().getCep());
        }
    }

    private static void editarImovel() throws SQLException {
        System.out.print("\nDigite o ID do Imóvel para editar: ");
        int id = lerInteiro();
        Imovel imovel = imovelDAO.buscarImovelPorId(id);
        if (imovel == null) {
            System.out.println("Imóvel não encontrado.");
            return;
        }

        System.out.println("[Editando Endereço]");
        System.out.print("CEP (Atual: " + imovel.getEndereco().getCep() + "): ");
        imovel.getEndereco().setCep(lerString());
        System.out.print("Rua (Atual: " + imovel.getEndereco().getRua() + "): ");
        imovel.getEndereco().setRua(lerString());
        System.out.print("Bairro (Atual: " + imovel.getEndereco().getBairro() + "): ");
        imovel.getEndereco().setBairro(lerString());
        System.out.print("Cidade (Atual: " + imovel.getEndereco().getCidade() + "): ");
        imovel.getEndereco().setCidade(lerString());
        System.out.print("Estado (Atual: " + imovel.getEndereco().getEstado() + "): ");
        imovel.getEndereco().setEstado(lerString());
        
        System.out.println("[Editando Imóvel]");
        System.out.print("Tipo (Atual: " + imovel.getTipo_imovel() + "): ");
        imovel.setTipo_imovel(lerString());

        imovelDAO.atualizarImovel(imovel);
        System.out.println("Imóvel atualizado com sucesso!");
    }
    
    private static void excluirImovel() throws SQLException {
        System.out.print("\nDigite o ID do Imóvel para excluir: ");
        int id = lerInteiro();

        Imovel imovel = imovelDAO.buscarImovelPorId(id);
        if (imovel == null) {
            System.out.println("Imóvel não encontrado.");
            return;
        }
        
        System.out.print("Tem certeza que deseja excluir o imóvel em " + imovel.getEndereco().getRua() + " (S/N)? ");
        if (lerString().equalsIgnoreCase("S")) {
            imovelDAO.excluirImovel(id);
            System.out.println("Imóvel excluído com sucesso.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static void gerenciarHidrometros() {
        while (true) {
            System.out.println("\n-- Gerenciar Hidrômetros --");
            System.out.println("1. Adicionar Hidrômetro");
            System.out.println("2. Listar Hidrômetros");
            System.out.println("3. Editar Hidrômetro");
            System.out.println("4. Excluir Hidrômetro");
            System.out.println("0. Voltar ao Menu Técnico");
            
            int opcao = lerInteiro();
            try {
                switch(opcao) {
                    case 1: adicionarHidrometro(); break;
                    case 2: listarHidrometros(); break;
                    case 3: editarHidrometro(); break;
                    case 4: excluirHidrometro(); break;
                    case 0: return;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void adicionarHidrometro() throws SQLException {
        System.out.println("\n[Adicionar Hidrômetro]");
        System.out.print("Nº de Série: ");
        String numSerie = lerString();
        System.out.print("Marca: ");
        String marca = lerString();
        System.out.print("Modelo: ");
        String modelo = lerString();

        Hidrometro h = new Hidrometro(numSerie, marca, modelo);
        hidrometroDAO.inserirHidrometro(h);
        System.out.println("Hidrômetro adicionado com sucesso!");
    }
    
    private static void listarHidrometros() throws SQLException {
        System.out.println("\n[Lista de Hidrômetros]");
        List<Hidrometro> lista = hidrometroDAO.listarHidrometros();
        if (lista.isEmpty()) {
            System.out.println("Nenhum hidrômetro cadastrado.");
            return;
        }
        for (Hidrometro h : lista) {
            System.out.printf("Série: %s | Marca: %s | Modelo: %s\n",
                h.getNum_serie_hidrometro(), h.getMarca(), h.getModelo());
        }
    }
    
    private static void editarHidrometro() throws SQLException {
        System.out.print("\nDigite o Nº de Série do hidrômetro para editar: ");
        String numSerie = lerString();
        Hidrometro h = hidrometroDAO.buscarHidrometroPorSerie(numSerie);
        if (h == null) {
            System.out.println("Hidrômetro não encontrado.");
            return;
        }
        
        System.out.println("O Nº de Série (PK) não pode ser editado.");
        System.out.print("Nova Marca (Atual: " + h.getMarca() + "): ");
        h.setMarca(lerString());
        System.out.print("Novo Modelo (Atual: " + h.getModelo() + "): ");
        h.setModelo(lerString());

        hidrometroDAO.atualizarHidrometro(h);
        System.out.println("Hidrômetro atualizado com sucesso!");
    }
    
    private static void excluirHidrometro() throws SQLException {
        System.out.print("\nDigite o Nº de Série do hidrômetro para excluir: ");
        String numSerie = lerString();
        
        Hidrometro h = hidrometroDAO.buscarHidrometroPorSerie(numSerie);
        if (h == null) {
            System.out.println("Hidrômetro não encontrado.");
            return;
        }

        System.out.print("Tem certeza que deseja excluir " + numSerie + " (S/N)? ");
        if (lerString().equalsIgnoreCase("S")) {
            hidrometroDAO.excluirHidrometro(numSerie);
            System.out.println("Hidrômetro excluído com sucesso.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static void gerenciarContratos() {
        while (true) {
            System.out.println("\n-- Gerenciar Contratos --");
            System.out.println("1. Adicionar Contrato");
            System.out.println("2. Listar Contratos");
            System.out.println("3. Editar Contrato");
            System.out.println("4. Excluir Contrato");
            System.out.println("0. Voltar ao Menu Técnico");
            
            int opcao = lerInteiro();
            try {
                switch(opcao) {
                    case 1: adicionarContrato(); break;
                    case 2: listarContratos(); break;
                    case 3: editarContrato(); break;
                    case 4: excluirContrato(); break;
                    case 0: return;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void adicionarContrato() throws Exception {
        System.out.println("\n[Adicionar Contrato]");
        System.out.print("ID do Cliente (Usuário): ");
        int codUsuario = lerInteiro();
        System.out.print("ID do Imóvel: ");
        int codImovel = lerInteiro();
        System.out.print("Nº de Série do Hidrômetro: ");
        String numSerie = lerString();
        System.out.print("Data Início (AAAA-MM-DD): ");
        Date dataInicio = new SimpleDateFormat("yyyy-MM-dd").parse(lerString());
        System.out.print("Status (Ex: ATIVO): ");
        String status = lerString();

        Usuario usuario = pfDAO.buscarPessoaFisicaPorId(codUsuario);
        if (usuario == null) usuario = pjDAO.buscarPessoaJuridicaPorId(codUsuario);
        if (usuario == null) throw new Exception("Usuário não encontrado.");

        Imovel imovel = imovelDAO.buscarImovelPorId(codImovel);
        if (imovel == null) throw new Exception("Imóvel não encontrado.");

        Hidrometro h = hidrometroDAO.buscarHidrometroPorSerie(numSerie);
        if (h == null) throw new Exception("Hidrômetro não encontrado.");

        Contrato c = new Contrato(0, usuario, imovel, h, dataInicio, status);
        contratoDAO.inserirContrato(c);
        System.out.println("Contrato (ID: " + c.getCod_contrato() + ") adicionado com sucesso!");
    }

    private static void listarContratos() throws SQLException {
        System.out.println("\n[Lista de Contratos]");
        List<Contrato> lista = contratoDAO.listarContratos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum contrato cadastrado.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Contrato c : lista) {
            System.out.printf("ID: %d | Status: %s | Cliente: %s | Imóvel ID: %d | Hidrômetro: %s | Início: %s\n",
                c.getCod_contrato(), c.getStatus(), c.getUsuario().getNome(), c.getImovel().getCod_imovel(), c.getHidrometro().getNum_serie_hidrometro(), sdf.format(c.getData_inicio()));
        }
    }

    private static void editarContrato() throws Exception {
        System.out.print("\nDigite o ID do Contrato para editar: ");
        int id = lerInteiro();
        Contrato c = contratoDAO.buscarContratoPorId(id);
        if (c == null) {
            System.out.println("Contrato não encontrado.");
            return;
        }

        System.out.print("Novo ID Cliente (Atual: " + c.getUsuario().getCodUsuario() + "): ");
        int codUsuario = lerInteiro();
        System.out.print("Novo ID Imóvel (Atual: " + c.getImovel().getCod_imovel() + "): ");
        int codImovel = lerInteiro();
        System.out.print("Novo Nº Série Hidrômetro (Atual: " + c.getHidrometro().getNum_serie_hidrometro() + "): ");
        String numSerie = lerString();
        
        String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(c.getData_inicio());
        System.out.print("Nova Data Início (AAAA-MM-DD) (Atual: " + dataAtual + "): ");
        Date dataInicio = new SimpleDateFormat("yyyy-MM-dd").parse(lerString());
        
        System.out.print("Novo Status (Atual: " + c.getStatus() + "): ");
        String status = lerString();

        Usuario usuario = pfDAO.buscarPessoaFisicaPorId(codUsuario);
        if (usuario == null) usuario = pjDAO.buscarPessoaJuridicaPorId(codUsuario);
        if (usuario == null) throw new Exception("Novo Usuário não encontrado.");

        Imovel imovel = imovelDAO.buscarImovelPorId(codImovel);
        if (imovel == null) throw new Exception("Novo Imóvel não encontrado.");

        Hidrometro h = hidrometroDAO.buscarHidrometroPorSerie(numSerie);
        if (h == null) throw new Exception("Novo Hidrômetro não encontrado.");
        
        c.setUsuario(usuario);
        c.setImovel(imovel);
        c.setHidrometro(h);
        c.setData_inicio(dataInicio);
        c.setStatus(status);

        contratoDAO.atualizarContrato(c);
        System.out.println("Contrato atualizado com sucesso!");
    }

    private static void excluirContrato() throws SQLException {
        System.out.print("\nDigite o ID do Contrato para excluir: ");
        int id = lerInteiro();

        Contrato c = contratoDAO.buscarContratoPorId(id);
        if (c == null) {
            System.out.println("Contrato não encontrado.");
            return;
        }

        System.out.print("Tem certeza que deseja excluir o contrato " + id + " (S/N)? ");
        if (lerString().equalsIgnoreCase("S")) {
            contratoDAO.excluirContrato(id);
            System.out.println("Contrato excluído com sucesso.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static void menuCliente() {
        System.out.print("\nDigite seu CPF ou CNPJ para continuar: ");
        String idDoc = lerString();

        boolean isPF = idDoc.length() <= 11;
        
        while (true) {
            System.out.println("\n== MENU DO CLIENTE (CPF/CNPJ: " + idDoc + ") ==");
            System.out.println("1. Ver Minhas Leituras");
            System.out.println("2. Ver Meus Alertas");
            System.out.println("3. Ver Meus Dados Cadastrais");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();
            try {
                switch (opcao) {
                    case 1: verMinhasLeituras(idDoc, isPF); break;
                    case 2: verMeusAlertas(idDoc, isPF); break;
                    case 3: verMeusDados(idDoc, isPF); break;
                    case 0: return;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void verMinhasLeituras(String idDoc, boolean isPF) throws SQLException {
        List<Leitura> leituras;
        if (isPF) {
            leituras = pfDAO.buscarLeiturasCNPJ(idDoc);
        } else {
            leituras = pjDAO.buscarLeiturasCNPJ(idDoc);
        }

        System.out.println("\n[Minhas Leituras]");
        if (leituras.isEmpty()) {
            System.out.println("Nenhuma leitura encontrada.");
            return;
        }
        for (Leitura l : leituras) {
            System.out.printf("Data: %s | Valor: %.2f | Hidrômetro: %s\n",
                l.getData_hora_leitura(), l.getValor_medido(), l.getHidrometro().getNum_serie_hidrometro());
        }
    }

    private static void verMeusAlertas(String idDoc, boolean isPF) throws SQLException {
        List<Alerta> alertas;
        if (isPF) {
            alertas = pfDAO.buscarAlertasproCNPJ(idDoc);
        } else {
            alertas = pjDAO.buscarAlertasproCNPJ(idDoc);
        }
        
        System.out.println("\n[Meus Alertas]");
        if (alertas.isEmpty()) {
            System.out.println("Nenhum alerta encontrado.");
            return;
        }
        for (Alerta a : alertas) {
            System.out.printf("Data: %s | Tipo: %s | (Ref. Leitura ID: %d)\n",
                a.getData_hora_alerta(), a.getTipo_alerta(), a.getLeitura().getCod_leitura());
        }
    }

    private static void verMeusDados(String idDoc, boolean isPF) throws SQLException {
        System.out.println("\n[Meus Dados Cadastrais]");
        if (isPF) {
            PessoaFisica pf = pfDAO.buscarPessoaFisicaPorCPF(idDoc);
            if (pf == null) { System.out.println("Cliente não encontrado."); return; }
            System.out.println("Tipo: Pessoa Física");
            System.out.println("ID: " + pf.getCodUsuario());
            System.out.println("Nome: " + pf.getNome());
            System.out.println("Email: " + pf.getEmail());
            System.out.println("CPF: " + pf.getCpf());
            System.out.println("Data Nasc.: " + pf.getDataNascimento());
        } else {
            PessoaJuridica pj = pjDAO.buscarPessoaJuridicaPorCNPJ(idDoc);
            if (pj == null) { System.out.println("Cliente não encontrado."); return; }
            System.out.println("Tipo: Pessoa Jurídica");
            System.out.println("ID: " + pj.getCodUsuario());
            System.out.println("Nome Fantasia: " + pj.getNome());
            System.out.println("Email: " + pj.getEmail());
            System.out.println("Razão Social: " + pj.getRazaoSocial());
            System.out.println("CNPJ: " + pj.getCnpj());
        }
    }

    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número: ");
            }
        }
    }
    
    private static String lerString() {
        return scanner.nextLine();
    }
    
    private static LocalDate lerData() {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.print("Formato de data inválido. Use AAAA-MM-DD: ");
            }
        }
    }
}