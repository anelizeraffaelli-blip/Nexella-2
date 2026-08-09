import React, { useState, useEffect } from 'react';
import {
  Sparkles,
  Search,
  Bell,
  Menu,
  X,
  User,
  PlusCircle,
  Building2,
  Users,
  Handshake,
  Heart,
  Globe,
  ArrowRight,
  Shield,
  CheckCircle2,
  Instagram,
  MessageSquare,
  Mail,
  LogOut,
  ChevronDown
} from 'lucide-react';

export interface LayoutUser {
  name: string;
  email?: string;
  businessName?: string;
  photoUrl?: string;
  isCorretora?: boolean;
  creci?: string;
}

export interface LayoutProps {
  children?: React.ReactNode;
  activeRoute?: string;
  onNavigate?: (route: string) => void;
  currentUser?: LayoutUser;
  onOpenCreateOpportunity?: () => void;
  onOpenProfile?: () => void;
  onOpenRegister?: () => void;
}

export const Layout: React.FC<LayoutProps> = ({
  children,
  activeRoute = 'dashboard',
  onNavigate,
  currentUser = {
    name: 'Anelize Raffaelli',
    email: 'anelize@nexella.com.br',
    businessName: 'Nexella Imóveis & Negócios',
    photoUrl: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80',
    isCorretora: true,
    creci: '42810-F'
  },
  onOpenCreateOpportunity,
  onOpenProfile,
  onOpenRegister
}) => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [newsletterEmail, setNewsletterEmail] = useState('');
  const [newsletterSubscribed, setNewsletterSubscribed] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 10);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const navItems = [
    { id: 'dashboard', label: 'Início', icon: Sparkles },
    { id: 'opportunities', label: 'Oportunidades', icon: Handshake, badge: 'Nova' },
    { id: 'radar', label: 'Radar de Negócios', icon: Users },
    { id: 'creci', label: 'Parcerias CRECI', icon: Building2 },
    { id: 'community', label: 'Comunidade', icon: Heart }
  ];

  const handleNavClick = (id: string) => {
    if (onNavigate) {
      onNavigate(id);
    }
    setIsMobileMenuOpen(false);
  };

  const handleNewsletterSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (newsletterEmail.trim()) {
      setNewsletterSubscribed(true);
      setTimeout(() => {
        setNewsletterEmail('');
        setNewsletterSubscribed(false);
      }, 4000);
    }
  };

  return (
    <div className="min-h-screen flex flex-col bg-slate-50 text-slate-800 font-sans antialiased">
      {/* Top Banner Accent */}
      <div className="h-1.5 w-full bg-gradient-to-r from-purple-700 via-rose-500 to-amber-400" />

      {/* Navigation Header */}
      <header
        className={`sticky top-0 z-40 transition-all duration-300 ${
          scrolled
            ? 'bg-white/95 backdrop-blur-md shadow-md border-b border-purple-100'
            : 'bg-white border-b border-slate-200'
        }`}
      >
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-20">
            {/* Brand Logo & Name */}
            <div
              onClick={() => handleNavClick('dashboard')}
              className="flex items-center gap-3 cursor-pointer group select-none"
            >
              <div className="w-11 h-11 rounded-2xl bg-gradient-to-tr from-purple-900 via-purple-700 to-rose-500 p-0.5 shadow-md group-hover:shadow-purple-200 transition-all duration-300">
                <div className="w-full h-full bg-white rounded-[14px] flex items-center justify-center">
                  <Sparkles className="w-6 h-6 text-purple-700 group-hover:scale-110 transition-transform duration-300" />
                </div>
              </div>
              <div className="flex flex-col">
                <span className="text-2xl font-black tracking-tight bg-gradient-to-r from-purple-900 via-purple-700 to-rose-600 bg-clip-text text-transparent">
                  Nexella
                </span>
                <span className="text-[11px] font-semibold tracking-wider text-slate-500 uppercase -mt-1">
                  Rede de Empreendedoras
                </span>
              </div>
            </div>

            {/* Desktop Search Bar */}
            <div className="hidden lg:flex items-center flex-1 max-w-xs mx-8">
              <div className="relative w-full">
                <Search className="w-4 h-4 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  placeholder="Buscar empreendedoras, serviços..."
                  className="w-full pl-10 pr-4 py-2 bg-slate-100/80 border border-slate-200 rounded-full text-xs text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-purple-600 focus:bg-white transition-all duration-200"
                />
              </div>
            </div>

            {/* Desktop Navigation Links */}
            <nav className="hidden md:flex items-center space-x-1 lg:space-x-2">
              {navItems.map((item) => {
                const IconComponent = item.icon;
                const isActive = activeRoute === item.id;
                return (
                  <button
                    key={item.id}
                    onClick={() => handleNavClick(item.id)}
                    className={`flex items-center gap-2 px-3.5 py-2 rounded-xl text-xs font-bold transition-all duration-200 relative ${
                      isActive
                        ? 'bg-purple-100/80 text-purple-900 shadow-sm'
                        : 'text-slate-600 hover:text-purple-800 hover:bg-purple-50'
                    }`}
                  >
                    <IconComponent
                      className={`w-4 h-4 ${
                        isActive ? 'text-purple-700' : 'text-slate-400'
                      }`}
                    />
                    <span>{item.label}</span>
                    {item.badge && (
                      <span className="ml-0.5 px-1.5 py-0.5 text-[9px] font-extrabold uppercase bg-rose-500 text-white rounded-full">
                        {item.badge}
                      </span>
                    )}
                  </button>
                );
              })}
            </nav>

            {/* Header Right Actions */}
            <div className="hidden md:flex items-center gap-3">
              {/* Create Opportunity Action Button */}
              <button
                onClick={onOpenCreateOpportunity}
                className="flex items-center gap-2 px-4 py-2.5 rounded-full text-xs font-bold text-white bg-gradient-to-r from-purple-800 via-purple-700 to-rose-600 hover:opacity-95 shadow-md shadow-purple-900/10 active:scale-[0.98] transition-all duration-200"
              >
                <PlusCircle className="w-4 h-4" />
                <span>Nova Oportunidade</span>
              </button>

              {/* Notification Bell */}
              <div className="relative">
                <button
                  onClick={() => setIsNotificationsOpen(!isNotificationsOpen)}
                  className="p-2.5 rounded-full text-slate-600 hover:text-purple-800 hover:bg-slate-100 transition-colors relative"
                  aria-label="Notificações"
                >
                  <Bell className="w-5 h-5" />
                  <span className="absolute top-2 right-2 w-2 h-2 rounded-full bg-rose-500 ring-2 ring-white animate-pulse" />
                </button>

                {/* Notifications Dropdown */}
                {isNotificationsOpen && (
                  <div className="absolute right-0 mt-3 w-80 bg-white rounded-2xl shadow-xl border border-slate-200 p-4 z-50 animate-in fade-in slide-in-from-top-2 duration-200">
                    <div className="flex items-center justify-between pb-3 border-b border-slate-100">
                      <h4 className="text-xs font-bold text-slate-800 uppercase tracking-wider">
                        Notificações Recentes
                      </h4>
                      <span className="text-[10px] font-bold text-purple-700 bg-purple-50 px-2 py-0.5 rounded-full">
                        3 Novas
                      </span>
                    </div>
                    <div className="divide-y divide-slate-100 max-h-64 overflow-y-auto">
                      <div className="py-2.5 cursor-pointer hover:bg-slate-50 transition-colors rounded-lg px-2">
                        <p className="text-xs font-semibold text-slate-800">
                          Nova parceira no CRECI!
                        </p>
                        <p className="text-[11px] text-slate-500">
                          Mariana Silva conectou com você em Cascavel.
                        </p>
                        <span className="text-[9px] text-slate-400 mt-1 block">
                          Há 10 min
                        </span>
                      </div>
                      <div className="py-2.5 cursor-pointer hover:bg-slate-50 transition-colors rounded-lg px-2">
                        <p className="text-xs font-semibold text-slate-800">
                          Oportunidade correspondente
                        </p>
                        <p className="text-[11px] text-slate-500">
                          Sua área de Fotografia teve 2 novas demandas.
                        </p>
                        <span className="text-[9px] text-slate-400 mt-1 block">
                          Há 1 hora
                        </span>
                      </div>
                    </div>
                  </div>
                )}
              </div>

              {/* User Profile Menu Button */}
              <div className="relative">
                <button
                  onClick={() => setIsProfileMenuOpen(!isProfileMenuOpen)}
                  className="flex items-center gap-2 p-1.5 rounded-full hover:bg-slate-100 transition-colors border border-slate-200"
                >
                  <img
                    src={currentUser.photoUrl}
                    alt={currentUser.name}
                    className="w-8 h-8 rounded-full object-cover ring-2 ring-purple-600/30"
                  />
                  <ChevronDown className="w-3.5 h-3.5 text-slate-500 pr-0.5" />
                </button>

                {/* Profile Dropdown Menu */}
                {isProfileMenuOpen && (
                  <div className="absolute right-0 mt-3 w-64 bg-white rounded-2xl shadow-xl border border-slate-200 p-3 z-50 animate-in fade-in slide-in-from-top-2 duration-200">
                    <div className="p-2 border-b border-slate-100 mb-2">
                      <p className="text-xs font-bold text-purple-900">
                        {currentUser.name}
                      </p>
                      <p className="text-[11px] text-slate-500 truncate">
                        {currentUser.businessName || currentUser.email}
                      </p>
                      {currentUser.isCorretora && (
                        <div className="mt-1.5 inline-flex items-center gap-1 px-2 py-0.5 rounded-full bg-amber-50 border border-amber-200 text-[10px] font-bold text-amber-800">
                          <Shield className="w-3 h-3 text-amber-600" />
                          <span>CRECI {currentUser.creci}</span>
                        </div>
                      )}
                    </div>

                    <button
                      onClick={() => {
                        setIsProfileMenuOpen(false);
                        if (onOpenProfile) onOpenProfile();
                      }}
                      className="w-full text-left px-3 py-2 rounded-xl text-xs font-semibold text-slate-700 hover:bg-purple-50 hover:text-purple-800 flex items-center gap-2 transition-colors"
                    >
                      <User className="w-4 h-4 text-purple-600" />
                      <span>Meu Perfil de Empreendedora</span>
                    </button>

                    {onOpenRegister && (
                      <button
                        onClick={() => {
                          setIsProfileMenuOpen(false);
                          onOpenRegister();
                        }}
                        className="w-full text-left px-3 py-2 rounded-xl text-xs font-semibold text-slate-700 hover:bg-purple-50 hover:text-purple-800 flex items-center gap-2 transition-colors"
                      >
                        <PlusCircle className="w-4 h-4 text-purple-600" />
                        <span>Cadastrar Nova Empreendedora</span>
                      </button>
                    )}
                  </div>
                )}
              </div>
            </div>

            {/* Mobile Hamburger Toggle Button */}
            <div className="flex md:hidden items-center gap-2">
              <button
                onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                className="p-2 rounded-xl text-slate-700 hover:bg-slate-100 focus:outline-none"
                aria-label="Menu Principal"
              >
                {isMobileMenuOpen ? (
                  <X className="w-6 h-6 text-purple-800" />
                ) : (
                  <Menu className="w-6 h-6 text-slate-700" />
                )}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile Slide-down Drawer Menu */}
        {isMobileMenuOpen && (
          <div className="md:hidden border-t border-slate-200 bg-white px-4 pt-3 pb-6 space-y-4 shadow-xl">
            {/* Mobile User Info Banner */}
            <div
              onClick={() => {
                if (onOpenProfile) onOpenProfile();
                setIsMobileMenuOpen(false);
              }}
              className="flex items-center gap-3 p-3 bg-purple-50 rounded-2xl cursor-pointer"
            >
              <img
                src={currentUser.photoUrl}
                alt={currentUser.name}
                className="w-10 h-10 rounded-full object-cover ring-2 ring-purple-600"
              />
              <div className="flex-1 min-w-0">
                <p className="text-xs font-bold text-purple-950 truncate">
                  {currentUser.name}
                </p>
                <p className="text-[11px] text-purple-700 truncate">
                  {currentUser.businessName}
                </p>
              </div>
              <User className="w-4 h-4 text-purple-700" />
            </div>

            {/* Mobile Search Bar */}
            <div className="relative w-full">
              <Search className="w-4 h-4 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Buscar empreendedoras em Cascavel..."
                className="w-full pl-10 pr-4 py-2.5 bg-slate-100 border border-slate-200 rounded-xl text-xs text-slate-800"
              />
            </div>

            {/* Mobile Nav Links */}
            <div className="space-y-1">
              {navItems.map((item) => {
                const IconComponent = item.icon;
                const isActive = activeRoute === item.id;
                return (
                  <button
                    key={item.id}
                    onClick={() => handleNavClick(item.id)}
                    className={`w-full flex items-center justify-between px-4 py-3 rounded-xl text-xs font-bold transition-colors ${
                      isActive
                        ? 'bg-purple-900 text-white'
                        : 'text-slate-700 hover:bg-slate-100'
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      <IconComponent
                        className={`w-4 h-4 ${
                          isActive ? 'text-rose-400' : 'text-slate-500'
                        }`}
                      />
                      <span>{item.label}</span>
                    </div>
                    {item.badge && (
                      <span className="px-2 py-0.5 text-[9px] font-extrabold bg-rose-500 text-white rounded-full">
                        {item.badge}
                      </span>
                    )}
                  </button>
                );
              })}
            </div>

            {/* Mobile Action Button */}
            <button
              onClick={() => {
                if (onOpenCreateOpportunity) onOpenCreateOpportunity();
                setIsMobileMenuOpen(false);
              }}
              className="w-full flex items-center justify-center gap-2 py-3 rounded-xl text-xs font-bold text-white bg-gradient-to-r from-purple-800 to-rose-600 shadow-md"
            >
              <PlusCircle className="w-4 h-4" />
              <span>Criar Nova Oportunidade</span>
            </button>
          </div>
        )}
      </header>

      {/* Main Content Body */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>

      {/* Footer Component */}
      <footer className="bg-slate-900 text-slate-300 border-t border-slate-800 pt-16 pb-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-10 pb-12 border-b border-slate-800">
            {/* Column 1: Brand Info & Socials */}
            <div className="lg:col-span-2 space-y-4">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-purple-600 to-rose-500 p-0.5">
                  <div className="w-full h-full bg-slate-900 rounded-[10px] flex items-center justify-center">
                    <Sparkles className="w-5 h-5 text-rose-400" />
                  </div>
                </div>
                <span className="text-2xl font-black tracking-tight text-white">
                  Nexella
                </span>
              </div>

              <p className="text-xs text-slate-400 leading-relaxed max-w-sm">
                Conectando mulheres. Criando oportunidades. A plataforma inteligente de networking e parcerias para empreendedoras de Cascavel/PR e região.
              </p>

              <div className="flex items-center gap-3 pt-2">
                <a
                  href="https://instagram.com"
                  target="_blank"
                  rel="noreferrer"
                  className="w-9 h-9 rounded-full bg-slate-800 hover:bg-rose-600 text-slate-300 hover:text-white flex items-center justify-center transition-colors"
                  aria-label="Instagram"
                >
                  <Instagram className="w-4 h-4" />
                </a>
                <a
                  href="https://whatsapp.com"
                  target="_blank"
                  rel="noreferrer"
                  className="w-9 h-9 rounded-full bg-slate-800 hover:bg-emerald-600 text-slate-300 hover:text-white flex items-center justify-center transition-colors"
                  aria-label="WhatsApp"
                >
                  <MessageSquare className="w-4 h-4" />
                </a>
                <a
                  href="mailto:contato@nexella.com.br"
                  className="w-9 h-9 rounded-full bg-slate-800 hover:bg-purple-600 text-slate-300 hover:text-white flex items-center justify-center transition-colors"
                  aria-label="E-mail"
                >
                  <Mail className="w-4 h-4" />
                </a>
              </div>
            </div>

            {/* Column 2: Ecosystem */}
            <div className="space-y-3">
              <h4 className="text-xs font-bold uppercase tracking-wider text-purple-400">
                Plataforma
              </h4>
              <ul className="space-y-2 text-xs">
                <li>
                  <button
                    onClick={() => handleNavClick('opportunities')}
                    className="hover:text-white transition-colors"
                  >
                    Mural de Oportunidades
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => handleNavClick('radar')}
                    className="hover:text-white transition-colors"
                  >
                    Radar de Negócios
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => handleNavClick('creci')}
                    className="hover:text-white transition-colors"
                  >
                    Rede Imobiliária CRECI
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => handleNavClick('community')}
                    className="hover:text-white transition-colors"
                  >
                    Vitrine de Serviços
                  </button>
                </li>
              </ul>
            </div>

            {/* Column 3: Community & Support */}
            <div className="space-y-3">
              <h4 className="text-xs font-bold uppercase tracking-wider text-rose-400">
                Comunidade
              </h4>
              <ul className="space-y-2 text-xs">
                <li>
                  <span className="hover:text-white cursor-pointer transition-colors">
                    Mentorias & Eventos
                  </span>
                </li>
                <li>
                  <span className="hover:text-white cursor-pointer transition-colors">
                    Histórias de Sucesso
                  </span>
                </li>
                <li>
                  <span className="hover:text-white cursor-pointer transition-colors">
                    Código de Conduta
                  </span>
                </li>
                <li>
                  <span className="hover:text-white cursor-pointer transition-colors">
                    Termos de Uso & Privacidade
                  </span>
                </li>
              </ul>
            </div>

            {/* Column 4: Newsletter */}
            <div className="space-y-3">
              <h4 className="text-xs font-bold uppercase tracking-wider text-amber-400">
                Boletim Informativo
              </h4>
              <p className="text-xs text-slate-400">
                Receba novidades e oportunidades semanais da rede em primeira mão.
              </p>

              <form onSubmit={handleNewsletterSubmit} className="space-y-2">
                <input
                  type="email"
                  value={newsletterEmail}
                  onChange={(e) => setNewsletterEmail(e.target.value)}
                  placeholder="Seu melhor e-mail"
                  required
                  className="w-full px-3.5 py-2 bg-slate-800 border border-slate-700 rounded-xl text-xs text-white placeholder-slate-500 focus:outline-none focus:ring-2 focus:ring-purple-500"
                />
                <button
                  type="submit"
                  className="w-full py-2 px-3 rounded-xl bg-purple-700 hover:bg-purple-600 text-white text-xs font-bold flex items-center justify-center gap-1.5 transition-colors"
                >
                  <span>Inscrever-se</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </button>
              </form>

              {newsletterSubscribed && (
                <div className="flex items-center gap-1.5 text-xs text-emerald-400 pt-1">
                  <CheckCircle2 className="w-4 h-4" />
                  <span>Inscrição confirmada com sucesso!</span>
                </div>
              )}
            </div>
          </div>

          {/* Footer Bottom Bar */}
          <div className="pt-8 flex flex-col sm:flex-row items-center justify-between text-[11px] text-slate-500 gap-4">
            <p>
              © {new Date().getFullYear()} Nexella Platform. Todos os direitos reservados.
            </p>
            <div className="flex items-center gap-1 text-slate-400">
              <span>Feito com</span>
              <Heart className="w-3.5 h-3.5 text-rose-500 fill-rose-500 inline" />
              <span>para mulheres que transformam o mercado.</span>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Layout;
