package com.example.data.repository

import com.example.data.local.dao.NexellaDao
import com.example.data.local.entity.BusinessEntity
import com.example.data.local.entity.ConnectionEntity
import com.example.data.local.entity.EllaMessageEntity
import com.example.data.local.entity.MeetingEntity
import com.example.data.local.entity.MeetingParticipantEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class NexellaRepository(private val dao: NexellaDao) {

    val allUsers: Flow<List<UserEntity>> = dao.getApprovedUsers()
    val allAdminUsers: Flow<List<UserEntity>> = dao.getAllUsers()
    val allProfiles: Flow<List<ProfileEntity>> = dao.getAllProfiles()
    val allBusinesses: Flow<List<BusinessEntity>> = dao.getAllBusinesses()
    val allOpportunities: Flow<List<OpportunityEntity>> = dao.getApprovedOpportunities()
    val allConnections: Flow<List<ConnectionEntity>> = dao.getAllConnections()
    val allMeetings: Flow<List<MeetingEntity>> = dao.getAllMeetings()
    val allEllaMessages: Flow<List<EllaMessageEntity>> = dao.getAllEllaMessages()

    val totalUsersCount: Flow<Int> = dao.getUserCount()
    val totalCorretorasCount: Flow<Int> = dao.getCorretoraCount()
    val totalOpportunitiesCount: Flow<Int> = dao.getOpportunityCount()
    val totalConnectionsCount: Flow<Int> = dao.getConnectionCount()
    val successfulDealsCount: Flow<Int> = dao.SuccessfulDealsCount()
    val totalMeetingsCount: Flow<Int> = dao.getMeetingCount()

    suspend fun getUserById(id: Long) = dao.getUserById(id)
    suspend fun insertUser(user: UserEntity) = dao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)
    suspend fun deleteUser(id: Long) = dao.deleteUser(id)

    fun getProfileByUserId(userId: Long): Flow<ProfileEntity?> = dao.getProfileByUserId(userId)
    fun getOpportunitiesByCityAndNeighborhood(city: String, neighborhood: String): Flow<List<OpportunityEntity>> =
        dao.getOpportunitiesByCityAndNeighborhood(city, neighborhood)
    suspend fun insertProfile(profile: ProfileEntity) = dao.insertProfile(profile)
    suspend fun updateProfile(profile: ProfileEntity) = dao.updateProfile(profile)
    suspend fun deleteProfile(id: Long) = dao.deleteProfile(id)

    fun getBusinessByUserId(userId: Long): Flow<BusinessEntity?> = dao.getBusinessByUserId(userId)
    suspend fun insertBusiness(business: BusinessEntity) = dao.insertBusiness(business)
    suspend fun updateBusiness(business: BusinessEntity) = dao.updateBusiness(business)
    suspend fun deleteBusiness(id: Long) = dao.deleteBusiness(id)

    suspend fun insertOpportunity(opp: OpportunityEntity) = dao.insertOpportunity(opp)
    suspend fun updateOpportunity(opp: OpportunityEntity) = dao.updateOpportunity(opp)
    suspend fun deleteOpportunity(id: Long) = dao.deleteOpportunity(id)

    suspend fun insertConnection(connection: ConnectionEntity) = dao.insertConnection(connection)
    suspend fun updateConnection(connection: ConnectionEntity) = dao.updateConnection(connection)

    suspend fun insertMeeting(meeting: MeetingEntity) = dao.insertMeeting(meeting)
    suspend fun getParticipantsForMeeting(meetingId: Long) = dao.getParticipantsForMeeting(meetingId)
    suspend fun isUserRegistered(meetingId: Long, userId: Long): Boolean {
        return dao.isUserRegisteredForMeeting(meetingId, userId) > 0
    }

    suspend fun joinMeeting(meetingId: Long, user: UserEntity) {
        val isReg = isUserRegistered(meetingId, user.id)
        if (!isReg) {
            dao.insertMeetingParticipant(
                MeetingParticipantEntity(
                    meetingId = meetingId,
                    userId = user.id,
                    userName = user.name,
                    userBusiness = user.businessName,
                    userPhoto = user.photoUrl
                )
            )
            val meeting = dao.getMeetingById(meetingId)
            if (meeting != null) {
                val updatedSpots = meeting.occupiedSpots + 1
                dao.updateMeeting(meeting.copy(occupiedSpots = updatedSpots))
            }
        }
    }

    suspend fun insertEllaMessage(msg: EllaMessageEntity) = dao.insertEllaMessage(msg)
    suspend fun clearEllaMessages() = dao.clearEllaMessages()

    suspend fun seedInitialDataIfNeeded() {
        val currentUsers = dao.getAllUsers().first()
        if (currentUsers.isEmpty()) {
            // Seed Initial Women Profiles in Cascavel, PR
            val user1 = UserEntity(
                name = "Maria Oliveira",
                photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80",
                businessName = "Maria Oliveira Imóveis",
                city = "Cascavel",
                neighborhood = "Centro",
                category = "Corretoras de Imóveis",
                services = "Venda de imóveis residenciais, lançamentos, avaliação e locação comercial.",
                description = "Corretora apaixonada por realizar sonhos em Cascavel. Especializada em lançamentos no Centro e Neva.",
                instagram = "@mariaoliveira.imoveis",
                whatsapp = "45999887766",
                allowWhatsapp = true,
                website = "https://mariaimoveis.com.br",
                email = "maria@mariaimoveis.com.br",
                status = "Aprovado",
                creci = "42810-F",
                specialities = "Imóveis Residenciais, Lançamentos Alto Padrão, Locação",
                procuro = "📸 Fotógrafa imobiliária para lançamentos, Designer para material de vendas",
                ofereco = "🤝 Parcerias em comissionamento, indicações de clientes e investidores",
                interests = "Networking imobiliário, Parcerias com arquitetas e designers",
                isFoundingMember = true,
                isCorretora = true
            )

            val user2 = UserEntity(
                name = "Fernanda Costa",
                photoUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=400&auto=format&fit=crop&q=80",
                businessName = "Fernanda Costa Fotografia",
                city = "Cascavel",
                neighborhood = "Neva",
                category = "Fotografia Imobiliária",
                services = "Ensaios imobiliários, vídeos com drone, tour virtual 360, fotos corporativas.",
                description = "Transformo imóveis e ambientes em experiências visuais marcantes para encantar compradores.",
                instagram = "@fe.costafoto",
                whatsapp = "45991234567",
                allowWhatsapp = true,
                website = "https://fecostafoto.com.br",
                email = "contato@fecostafoto.com.br",
                status = "Aprovado",
                creci = null,
                specialities = "Fotografia de Arquitetura, Vídeo Imobiliário, Drone",
                procuro = "🏢 Corretoras e imobiliárias para contratos fixos e parcerias",
                ofereco = "📸 Descontos em pacotes de fotografia para membros Nexella",
                interests = "Fotografia corporativa, Mercado imobiliário",
                isFoundingMember = true,
                isCorretora = false
            )

            val user3 = UserEntity(
                name = "Juliana Martins",
                photoUrl = "https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?w=400&auto=format&fit=crop&q=80",
                businessName = "Martins Arquitetura & Home Staging",
                city = "Cascavel",
                neighborhood = "Centro",
                category = "Arquitetura & Design",
                services = "Projetos de interiores, reformas, consultoria de Home Staging para valorização de imóveis.",
                description = "Ajudamos você a vender mais rápido através de Home Staging inteligente e decoração afetiva.",
                instagram = "@julianamartins.arq",
                whatsapp = "45998765432",
                allowWhatsapp = true,
                website = "",
                email = "juliana@martinsarq.com.br",
                status = "Aprovado",
                creci = null,
                specialities = "Home Staging, Decoração de Interiores, Consultoria Express",
                procuro = "🔑 Corretoras de imóveis para valorização de carteira, Marcenaria parceira",
                ofereco = "🏡 Avaliação visual gratuita de imóveis à venda para parceiras Nexella",
                interests = "Arquitetura comercial, Valorização imobiliária",
                isFoundingMember = true,
                isCorretora = false
            )

            val user4 = UserEntity(
                name = "Camila Rocha",
                photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
                businessName = "Studio Rocha Design",
                city = "Cascavel",
                neighborhood = "Parque Verde",
                category = "Branding & Social Media",
                services = "Identidade visual, posicionamento de marca, gestão de mídias sociais para empreendedoras.",
                description = "Construo marcas fortes e elegantes para mulheres que lideram seus próprios negócios.",
                instagram = "@studiorochadesign",
                whatsapp = "45997654321",
                allowWhatsapp = true,
                website = "https://studiorocha.com",
                email = "camila@studiorocha.com",
                status = "Aprovado",
                creci = null,
                specialities = "Design Gráfico, Branding Pessoal, Reels & Vídeo Short",
                procuro = "💼 Profissionais autônomas que desejam elevar seu posicionamento",
                ofereco = "🎨 Diagnóstico gratuito de perfil no Instagram para empreendedoras",
                interests = "Marketing digital, Parcerias de criação",
                isFoundingMember = true,
                isCorretora = false
            )

            val user5 = UserEntity(
                name = "Patrícia Lima",
                photoUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400&auto=format&fit=crop&q=80",
                businessName = "Lima Contabilidade Estratégica",
                city = "Cascavel",
                neighborhood = "Cancelli",
                category = "Contabilidade & Finanças",
                services = "Abertura de MEI/Empresas, planejamento tributário, gestão financeira para autônomas.",
                description = "Descomplico as finanças e impostos do seu negócio com atendimento humanizado.",
                instagram = "@limacontabilidade.cascavel",
                whatsapp = "45996543210",
                allowWhatsapp = true,
                website = "",
                email = "patricia@limacontabilidade.com.br",
                status = "Aprovado",
                creci = null,
                specialities = "MEI, Simples Nacional, Planejamento Tributário",
                procuro = "👩‍💻 Novas empreendedoras abrindo negócios em Cascavel",
                ofereco = "📊 Análise de enquadramento tributário com 20% OFF para a comunidade",
                interests = "Finanças femininas, Crescimento empresarial",
                isFoundingMember = true,
                isCorretora = false
            )

            val id1 = dao.insertUser(user1)
            val id2 = dao.insertUser(user2)
            val id3 = dao.insertUser(user3)
            val id4 = dao.insertUser(user4)
            val id5 = dao.insertUser(user5)

            // Seed Initial Radar Opportunities
            dao.insertOpportunity(
                OpportunityEntity(
                    title = "Procuro fotógrafa imobiliária para apartamentos no Centro",
                    description = "Preciso de uma profissional para fotografar 3 apartamentos recém-reformados no Centro de Cascavel. Fotos de alta resolução e entrega em 48h.",
                    category = "Fotografia Imobiliária",
                    city = "Cascavel",
                    neighborhood = "Centro",
                    authorId = id1,
                    authorName = "Maria Oliveira",
                    authorBusiness = "Maria Oliveira Imóveis",
                    authorPhoto = user1.photoUrl,
                    type = "Procuro profissional",
                    isImobiliario = true,
                    status = "Aprovada"
                )
            )

            dao.insertOpportunity(
                OpportunityEntity(
                    title = "Busco parceira de Home Staging para imóveis encalhados",
                    description = "Gostaria de firmar parceria com profissional de Home Staging para preparar 2 casas no Parque Verde e acelerar a venda.",
                    category = "Arquitetura & Design",
                    city = "Cascavel",
                    neighborhood = "Parque Verde",
                    authorId = id1,
                    authorName = "Maria Oliveira",
                    authorBusiness = "Maria Oliveira Imóveis",
                    authorPhoto = user1.photoUrl,
                    type = "Procuro parceira",
                    isImobiliario = true,
                    status = "Aprovada"
                )
            )

            dao.insertOpportunity(
                OpportunityEntity(
                    title = "Ofereço pacotes especiais de Branding para Corretoras",
                    description = "Estou lançando uma mentoria visual rápida para criar cartões digitais, flyers e posts de lançamentos para corretoras de Cascavel.",
                    category = "Branding & Social Media",
                    city = "Cascavel",
                    neighborhood = "Parque Verde",
                    authorId = id4,
                    authorName = "Camila Rocha",
                    authorBusiness = "Studio Rocha Design",
                    authorPhoto = user4.photoUrl,
                    type = "Ofereço serviço",
                    isImobiliario = true,
                    status = "Aprovada"
                )
            )

            // Seed Initial Meetings (Encontros Presenciais)
            val meeting1Id = dao.insertMeeting(
                MeetingEntity(
                    name = "Café Nexella — Centro: Conexões Imobiliárias",
                    date = "Próxima Terça",
                    time = "08:30 - 10:00",
                    location = "Café & Co. - Rua Paraná, Centro",
                    neighborhood = "Centro",
                    city = "Cascavel",
                    description = "Primeiro encontro presencial de corretoras, fotógrafas e arquitetas do Centro de Cascavel para troca de indicações e parcerias.",
                    totalSpots = 15,
                    occupiedSpots = 6,
                    isCorretoraFocus = true
                )
            )

            dao.insertMeeting(
                MeetingEntity(
                    name = "Rodada de Negócios Nexella — Neva",
                    date = "Próxima Quinta",
                    time = "19:00 - 21:00",
                    location = "Espaço Co-Working Neva",
                    neighborhood = "Neva",
                    city = "Cascavel",
                    description = "Apresente seu negócio em 2 minutos, distribua cartões e crie conexões reais para acelerar suas vendas no bairro Neva e região.",
                    totalSpots = 20,
                    occupiedSpots = 8,
                    isCorretoraFocus = false
                )
            )

            // Seed Initial Connection Sample
            dao.insertConnection(
                ConnectionEntity(
                    requesterId = id1,
                    requesterName = "Maria Oliveira",
                    recipientId = id2,
                    recipientName = "Fernanda Costa",
                    recipientBusiness = "Fernanda Costa Fotografia",
                    recipientPhoto = user2.photoUrl,
                    recipientCategory = "Fotografia Imobiliária",
                    recipientWhatsapp = "45991234567",
                    recipientNeighborhood = "Neva",
                    date = "Ontem",
                    origin = "Radar Nexella",
                    generatedOpportunity = true,
                    notes = "Fechamos contrato para ensaios mensais dos imóveis no Centro!"
                )
            )

            // Initial ELLA Welcome Message
            dao.insertEllaMessage(
                EllaMessageEntity(
                    sender = "ELLA",
                    text = "Olá! Eu sou a Ella 💜\nEstou aqui para ajudar você a encontrar conexões e oportunidades dentro da Nexella."
                )
            )
        }
    }
}
