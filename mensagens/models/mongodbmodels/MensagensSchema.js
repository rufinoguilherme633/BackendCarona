const mongoose = require("mongoose")



const MensagensSchema = mongoose.Schema({
    
    id_sender: {
        type: mongoose.Schema.Types.Decimal128,
        require:true
    },
    id_receiver: {
        type: mongoose.Schema.Types.Decimal128,
        require:true
    },
    id_solicitacao:{
        type: mongoose.Schema.Types.Decimal128,
        require:true
    },
    data: {
        type: Date,
        require:true
    },
    data_atualizacao:{
         type: Date,
         default:null
    },
     message:{
        type:String,
        require:true
    }
})


module.exports = mongoose.model("mensagens",MensagensSchema)