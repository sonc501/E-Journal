const user = localStorage.getItem("user");
const token = localStorage.getItem("token");
const role = localStorage.getItem("role");

const author = {
  submittedPapers: [],
  editPaperId: "",
  paperDetail: {},
  newPaper: {
    paperTitle: "",
    paperSummary: "",
    paperJournal: { journalName: "", journalId: "" },
    paperPdfFile: { fileName: "", file: "" },
    paperFields: [],
  },
  search: {
    keyword: "",
    startDate: "",
    endDate: "",
    status: "ALL",
    page: 1,
    numOfPage: 1,
    totalFound: 0,
    fields: [],
    result: [],
  },
  paperStatusOptions: ["PENDING", "REVIEWING", "ACCEPTED", "REJECTED", "PUBLISH"],
};

const reviewer = {
  invitations: [],
  invitationDetail: {},
  reviewReports: [],
  reviewDetail: {},
  editReviewId: "",
  searchInvitation: {
    title: "",
    status: "ALL",
    startDate: "",
    page: 1,
    numOfPage: 1,
    totalFound: 0,
    result: [],
  },
  searchReview: {
    title: "",
    status: "ALL",
    verdict: "ALL",
    page: 1,
    numOfPage: 1,
    totalFound: 0,
    result: [],
  },
  newReview: {
    reviewPaper: {},
    reviewNote: "",
    reviewGrade: 0,
    reviewConfidentiality: 0,
    reviewVerdict: "ACCEPTED",
  }
}

const member = {
  search: {
    keyword: "",
    type: "Journal",
    options: ["Journal", "Paper"],
    fields: [],
    result: [],
    page: 1,
    numOfPage: 1,
    totalFound: 0,
  },
  journalDetailId: '',
  journalSlug: '',
  journal: {},
  profile: {},
  issuePublishes: {},
  publish: null,
  authorProfile: null,
  authorPublish: [],
};

const manager = {
  searchPaper: {
    keyword: "",
    status: "ALL",
    startDate: "",
    page: 1,
    numOfPage: 1,
    result: [],
    totalFound: 0,
  },
  publishIssue: {
    acceptedPapers: [],
    publishes: [],
    startDate: "",
    endDate: "",
    latestIssue: {},
    confirm: false,
    result: [],
    page: 1,
    numOfPage: 1,
    totalFound: 0,
  },
  sentPapers: [],
  sentInvitations: [],
  availableReviewers: [],
  paper: {},
  journal: {},
  issues: {},
}

const base = {
  role: role,
  isLoading: false,
  showAlert: false,
  alertText: "",
  alertType: "",
  user: user ? user : "",
  token: token,
  showSidebar: false,
  viewType: "author",
  fields: [],
  invitationStatus: ["PENDING", "ACCEPTED", "REJECTED"],
};

const initialState = {
  base,
  author,
  reviewer,
  member,
  manager,
};

export { initialState, base, reviewer, author, member, manager };
