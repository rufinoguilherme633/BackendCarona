const express = require("express");
const db = require("./database/dbMongodb");

const jwt = require("jsonwebtoken");
const bodyParser = require("body-parser");
const { sendMessage } = require("./service/MensagensService");
const WebSocket = require("ws");
const User = require("./models/mysqlmodels/User");
const MensagensSchema = require("./models/mongodbmodels/MensagensSchema"); // importa o model
const { Json } = require("sequelize/lib/utils");

const app = express();

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

function checkToken(token) {
  try {
    return jwt.verify(token, process.env.SECRET);
  } catch (error) {
    throw new Error("Token invalido");
  }
}

const findById = async (id) => {
  try {
    const existUser = await User.findOne({ where: { id_usuario: id } });
    return existUser;
  } catch (error) {
    console.error("Erro ao buscar usuário:", error);
    return null;
  }
};

const port = 9000;
const socket = new WebSocket.Server({ port: port });
let users = new Map();

socket.on("connection", async (ws, req) => {
  const token = req.headers["sec-websocket-protocol"];

  if (!token) {
    console.log("Conexão recusada: token ausente");
    ws.close(401, "Token ausente");
    return;
  }

  try {
    const decoded = checkToken(token);
    const existUser = await findById(decoded.sub);

    if (!existUser) {
      ws.close(404, "usuario não encontrado");
      return;
    }

    ws.userId = existUser.id_usuario;
    users.set(existUser.id_usuario, ws);

    ws.on("message", async (message) => {
      console.log("mensagem recebida do usuario" + ws.userId);

      const data = JSON.parse(message.toString());

      const existUserReciever = await findById(data.receiver);

      if (!existUserReciever) {
        ws.send(
          JSON.stringify({
            sucesso: false,
            erro: "Usuário de destino não encontrado",
          })
        );
        return;
      }

      try {
        let messageData = {
          id_sender: ws.userId,
          id_receiver: data.receiver,
          id_solicitacao: data.id_solicitacao,
          data: data.data,
          data_atualizacao: null,
          message: data.message,
        };

        console.log("Salvando mensagem... ");
        const messageSave = await new MensagensSchema(messageData);
        await messageSave.save();

        let reciverConect = users.get(messageData.id_receiver);

        console.log(reciverConect);
        if (reciverConect) {
          reciverConect.send(
            JSON.stringify({
              sucesso: true,
              tipo: "mensagem_recebida",
              mensagem: messageData,
            })
          );
        }

        ws.send(
          JSON.stringify({
            sucesso: true,
            tipo: "mensagem_confirmada",
            mensagem: "Mensagem salva e enviada com sucesso!",
          })
        );

        console.log("✅ Mensagem salva e enviada com sucesso!");
      } catch (error) {
        console.error("Erro ao processar mensagem:", error);
        ws.send(
          JSON.stringify({
            sucesso: false,
            erro: "Erro ao salvar ou enviar mensagem.",
          })
        );
      }
    });
    ws.on("close", () => {
      users.delete(ws.userId);
      console.log(`🔌 Usuário ${ws.userId} desconectado.`);
    });
  } catch (error) {
    console.error("❌ Erro de autenticação:", error.message);
    ws.close(403, "Token inválido");
  }
});
